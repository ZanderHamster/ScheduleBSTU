package ru.lekveishvili.david.schedulebstu.screens.home;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import butterknife.BindView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.models.Day;
import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.models.EventType;
import ru.lekveishvili.david.schedulebstu.models.Group;
import ru.lekveishvili.david.schedulebstu.models.Room;
import ru.lekveishvili.david.schedulebstu.models.Subject;
import ru.lekveishvili.david.schedulebstu.models.Teacher;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;
import ru.lekveishvili.david.schedulebstu.screens.home.adapters.HomeSectionAdapter;

public class HomeController extends BaseController {
    private Realm realm;
    private List<List<Event>> weeks = new ArrayList<>();
    @BindView(R.id.home_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_home, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        realm = Realm.getDefaultInstance();
        configureToolbar();
        setModel();
    }

    private void setModel() {
        Date startPeriod = realm.where(Event.class).minimumDate("startEvent");
        Date endPeriod = realm.where(Event.class).maximumDate("endEvent");
        if (startPeriod != null && endPeriod != null) {
            Calendar startWeek = Calendar.getInstance();
            Calendar endWeek = Calendar.getInstance();
            startWeek.setTime(startPeriod);
            startWeek.set(Calendar.HOUR_OF_DAY, 1);
            endWeek.setTime(startPeriod);

            while (endWeek.getTime().getTime() < endPeriod.getTime()) {
                while (endWeek.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    endWeek.add(Calendar.DATE, 1);
                }
                startWeek.set(Calendar.HOUR_OF_DAY, 1);
                Date dateStartWeek = startWeek.getTime();
                Date dateEndWeek = endWeek.getTime();
                RealmResults<Event> groups = realm.where(Event.class)
                        .equalTo("groups.name", "13-ИВТ1")
                        .between("startEvent", dateStartWeek, dateEndWeek)
                        .findAllSorted("startEvent");
                List<Event> eventList = realm.copyFromRealm(groups);
                weeks.add(eventList);
                endWeek.add(Calendar.DATE, 1);
                Date time = endWeek.getTime();
                startWeek.setTime(time);
            }

            Pager pager = new Pager(weeks);
            viewPager.setAdapter(pager);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    viewPager.setCurrentItem(position);
                    pager.setCurrentWeek(position);
                    int t = position;
                    t += 1;

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private void configureToolbar() {
        toolbarTitle.setText(getResources().getString(R.string.bottom_menu_home));
    }
}
