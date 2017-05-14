package ru.lekveishvili.david.schedulebstu.screens.home;

import android.support.annotation.NonNull;
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
    private final SectionedRecyclerViewAdapter specificationsSectionAdapter = new SectionedRecyclerViewAdapter();
    private Realm realm;
    @BindView(R.id.home_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.home_recycler)
    RecyclerView homeRecycler;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_home, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
//        ScheduleBSTUApplication
//                .getAppComponent(view.getContext())
//                .plus(new HomeComponent.GroupControllerModule())
//                .inject(this);
        realm = Realm.getDefaultInstance();
        configureToolbar();
        configureRecycler();
        setModel();
    }

    private void setModel() {
//        DeleteAllTable();
        realm.beginTransaction();

//        Event manageEvent = realm.copyToRealm(event);
//        RealmResults<Event> requestGroup = realm.where(Event.class)
//                .equalTo("group.name", manageGroup.getName())
//                .findAll();
        RealmResults<Group> requestEvent = realm.where(Group.class).findAll();
//
        if (!requestEvent.isEmpty()) {
            int t = 3;
            String s = String.valueOf(t);
        } else {
            int t = 5;
            String s = String.valueOf(t);
        }
        realm.commitTransaction();

        specificationsSectionAdapter.removeAllSections();
        specificationsSectionAdapter.addSection(new HomeSectionAdapter(convertDateToString(createTestDay().getDate()), createTestDay().getEventList()));
        specificationsSectionAdapter.addSection(new HomeSectionAdapter(convertDateToString(createTestDay2().getDate()), createTestDay2().getEventList()));
        specificationsSectionAdapter.addSection(new HomeSectionAdapter(convertDateToString(createTestDay2().getDate()), createTestDay2().getEventList()));
        specificationsSectionAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
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

    private Day createTestDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(2017, 4, 9, 10, 0, 0);
        Date date = cal.getTime();

        return Day.newBuilder()
                .withName("среда, 10 мая")
                .withDate(date)
                .withEventItems(createTestEvents(date))
                .build();
    }

    private Day createTestDay2() {
        Calendar cal = Calendar.getInstance();
        cal.set(2017, 4, 10, 10, 0, 0);
        Date date = cal.getTime();
        return Day.newBuilder()
                .withName("четверг, 11 мая")
                .withDate(date)
                .withEventItems(createTestEvents(date))
                .build();
    }

    private List<Event> createTestEvents(Date date) {
        List<Event> eventList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), date.getMonth(), date.getDay(), 22, 10);
        Date date1 = cal.getTime();

        Calendar cal2 = Calendar.getInstance();
        cal2.set(date.getYear(), date.getMonth(), date.getDay(), 22, 30);
        Date date2 = cal2.getTime();

        Calendar cal3 = Calendar.getInstance();
        cal3.set(date.getYear(), date.getMonth(), date.getDay(), 23, 10);
        Date date3 = cal3.getTime();


        Calendar cal4 = Calendar.getInstance();
        cal4.set(date.getYear(), date.getMonth(), date.getDay(), 23, 30);
        Date date4 = cal2.getTime();

        Event build = Event.newBuilder()
                .withEventType(EventType.newBuilder()
                        .withName("Лекция")
                        .build())
                .withGroup(Group.newBuilder()
                        .withName("13-ИВТ1")
                        .build())
                .withRoom(Room.newBuilder()
                        .withName("420B")
                        .build())
                .withSubject(Subject.newBuilder()
                        .withName("Программирование")
                        .build())
                .withTeacher(Teacher.newBuilder()
                        .withFirstName("Иванов")
                        .withSecondName("Иван")
                        .withThirdName("Иванович")
                        .build())
                .withStartEvent(date1)
                .withEndEvent(date2)
                .build();

        eventList.add(Event.newBuilder(build)
                .withStartEvent(date3)
                .withEndEvent(date4)
                .build()
        );

        eventList.add(Event.newBuilder()
                .withEventType(EventType.newBuilder()
                        .withName("Лекция")
                        .build())
                .withGroup(Group.newBuilder()
                        .withName("13-ИВТ1")
                        .build())
                .withRoom(Room.newBuilder()
                        .withName("420B")
                        .build())
                .withSubject(Subject.newBuilder()
                        .withName("Программирование")
                        .build())
                .withTeacher(Teacher.newBuilder()
                        .withFirstName("Иванов")
                        .withSecondName("Иван")
                        .withThirdName("Иванович")
                        .build())
                .withStartEvent(date1)
                .withEndEvent(date2)
                .build()
        );

        eventList.add(Event.newBuilder()
                .withEventType(EventType.newBuilder()
                        .withName("Семинар")
                        .build())
                .withGroup(Group.newBuilder()
                        .withName("14-ИВТ1")
                        .build())
                .withRoom(Room.newBuilder()
                        .withName("219")
                        .build())
                .withSubject(Subject.newBuilder()
                        .withName("Физра")
                        .build())
                .withTeacher(Teacher.newBuilder()
                        .withFirstName("Петров")
                        .withSecondName("Петр")
                        .withThirdName("Петрович")
                        .build())
                .withStartEvent(date1)
                .withEndEvent(date2)
                .build()
        );
        eventList.add(Event.newBuilder()
                .withEventType(EventType.newBuilder()
                        .withName("Семинар")
                        .build())
                .withGroup(Group.newBuilder()
                        .withName("14-ИВТ1")
                        .build())
                .withRoom(Room.newBuilder()
                        .withName("219")
                        .build())
                .withSubject(Subject.newBuilder()
                        .withName("Физра")
                        .build())
                .withTeacher(Teacher.newBuilder()
                        .withFirstName("Петров")
                        .withSecondName("Петр")
                        .withThirdName("Петрович")
                        .build())
                .withStartEvent(date1)
                .withEndEvent(date2)
                .build()
        );
        return eventList;
    }

    private void configureRecycler() {
        homeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeRecycler.setAdapter(specificationsSectionAdapter);
    }

    private void configureToolbar() {
        toolbarTitle.setText(getResources().getString(R.string.bottom_menu_home));
    }
}
