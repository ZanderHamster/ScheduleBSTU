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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.screens.home.adapters.HomeSectionAdapter;

public class NewPager extends PagerAdapter {
    private final static int WEEKS_IN_YEAR = 52;
    List<Event> eventList;


    private final SectionedRecyclerViewAdapter specificationsSectionAdapter = new SectionedRecyclerViewAdapter();
    public ItemClickListener onItemClickListenerr;


    public NewPager(List<Event> eventList) {
        this.eventList = eventList;
    }

    public void setEventList(List<Event> list) {
        eventList = list;
    }

    @Override
    public int getCount() {
        return WEEKS_IN_YEAR;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


//    private void executeGroup() {
//        new GetEventWeekUseCase(apiService, token, strDate, person)
//                .executeTeacher()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::setModelGroup);
//    }


//    private void setModelGroup(RealmList<Event> events) {
//        realm.beginTransaction();
//        RealmResults<Event> all = realm.where(Event.class).findAll();
//        all.deleteAllFromRealm();
//        for (int i = 0; i < events.size(); i++) {
//            realm.copyToRealm(events.get(i));
//        }
//        realm.commitTransaction();
//        RealmResults<Authorization> authorizations = realm.where(Authorization.class).findAll();
//        String fullName = authorizations.get(0).getFullName();
//
//        RealmResults<Event> groups = realm.where(Event.class)
//                .equalTo("groups.name", person)
//                .between("startEvent", getStartWeek(cal.getTime()), getEndWeek(cal.getTime()))
//                .findAllSorted("startEvent");
//        eventList.clear();
//        eventList = realm.copyFromRealm(groups);
//    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecyclerView recyclerView = new RecyclerView(container.getContext());
        if (eventList.size() != 0) {
            specificationsSectionAdapter.removeAllSections();
            boolean flag = false;
            Calendar calFirstEvent = Calendar.getInstance();
            calFirstEvent.setTime(eventList.get(0).getStartEvent());

            List<Event> eventsFromOneDay = new ArrayList<>();
            for (int i = 0; i < eventList.size(); i++) {
                Event event = eventList.get(i);
                Calendar calCurrentEvent = Calendar.getInstance();
                calCurrentEvent.setTime(event.getStartEvent());

                if (calFirstEvent.get(Calendar.DAY_OF_YEAR) == calCurrentEvent.get(Calendar.DAY_OF_YEAR)) {
                    eventsFromOneDay.add(event);
                } else {
                    flag = true;
                    calFirstEvent.add(Calendar.DATE, 1);
                    HomeSectionAdapter section = new HomeSectionAdapter(convertDateToString(
                            new ArrayList<>(eventsFromOneDay).get(0).getStartEvent()),
                            new ArrayList<>(eventsFromOneDay));
                    specificationsSectionAdapter.addSection(
                            section);
                    section.setOnItemClickListener(item -> onItemClickListenerr.onClick(item));
                    eventsFromOneDay.clear();
                    eventsFromOneDay.add(event);
                }
                if (i == eventList.size() - 1) {
                    flag = false;
                }


            }
            if (!flag) {
                HomeSectionAdapter section = new HomeSectionAdapter(convertDateToString(
                        new ArrayList<>(eventsFromOneDay).get(0).getStartEvent()),
                        new ArrayList<>(eventsFromOneDay));
                specificationsSectionAdapter.addSection(
                        section);
                section.setOnItemClickListener(item -> onItemClickListenerr.onClick(item));

            }
        }
        specificationsSectionAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(specificationsSectionAdapter);
        container.addView(recyclerView);

        return recyclerView;
    }

    void setOnItemClickListener(ItemClickListener listener) {
        this.onItemClickListenerr = listener;
    }

    interface ItemClickListener {
        void onClick(Event event);
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
