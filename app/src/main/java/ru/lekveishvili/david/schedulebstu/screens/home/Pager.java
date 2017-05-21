package ru.lekveishvili.david.schedulebstu.screens.home;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.screens.home.adapters.HomeSectionAdapter;

public class Pager extends PagerAdapter {
    private List<List<Event>> weeks = new ArrayList<>();
    private final SectionedRecyclerViewAdapter specificationsSectionAdapter = new SectionedRecyclerViewAdapter();

    public Pager(List<List<Event>> weeks) {
        this.weeks = weeks;
    }

    @Override
    public int getCount() {
        return weeks.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecyclerView recyclerView = new RecyclerView(container.getContext());
        specificationsSectionAdapter.removeAllSections();
        List<Event> eventList = weeks.get(position);
        for (int i = 0; i < weeks.size(); i++) {
//            Collections.reverse(list);
            Date startDay = weeks.get(i).get(0).getStartEvent();
            Calendar  cal = Calendar.getInstance();
            cal.setTime(startDay);
            int i1 = cal.get(Calendar.DAY_OF_YEAR);
            List<Event> eventsFromOneDay = new ArrayList<>();
            List<Event> tmp = new ArrayList<>(eventsFromOneDay);

            for (int j = 0; j < weeks.get(i).size(); j++) {
                List<Event> eventWeekList = weeks.get(i);
                Date startEvent = eventWeekList.get(j).getStartEvent();
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(startEvent);

                if (i1 == cal2.get(Calendar.DAY_OF_YEAR)) {
                    eventsFromOneDay.add(eventWeekList.get(j));
                } else {
                    cal.add(Calendar.DATE, 1);
                    eventsFromOneDay.clear();
                    eventsFromOneDay.add(eventWeekList.get(j));
                }
                List<Event> list = weeks.get(i);
            }
            specificationsSectionAdapter.addSection(
                    new HomeSectionAdapter(convertDateToString(
                            new ArrayList<>(eventsFromOneDay).get(0).getStartEvent()),
                            new ArrayList<>(eventsFromOneDay)));
        }
        specificationsSectionAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(specificationsSectionAdapter);
        container.addView(recyclerView);
        return recyclerView;
    }

    private String convertDateToString(Date date) {
        DateFormat dateMonth = new SimpleDateFormat("d", new Locale("ru", "RU"));
        DateFormat month = new SimpleDateFormat("M", new Locale("ru", "RU"));
        String dayOfWeek = (new SimpleDateFormat("EEEE", new Locale("ru", "RU"))).format(date);
        String monthOfYear;
        switch (Integer.valueOf(month.format(date))) {
            case 1:
                monthOfYear = "января";
                break;
            case 2:
                monthOfYear = "февраля";
                break;
            case 3:
                monthOfYear = "марта";
                break;
            case 4:
                monthOfYear = "апреля";
                break;
            case 5:
                monthOfYear = "мая";
                break;
            case 6:
                monthOfYear = "июня";
                break;
            case 7:
                monthOfYear = "июля";
                break;
            case 8:
                monthOfYear = "августа";
                break;
            case 9:
                monthOfYear = "сентября";
                break;
            case 10:
                monthOfYear = "октября";
                break;
            case 11:
                monthOfYear = "ноября";
                break;
            case 12:
                monthOfYear = "декабря";
                break;
            default:
                monthOfYear = "";
                break;
        }
        return dayOfWeek + ", " + dateMonth.format(date) + " " + monthOfYear;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
