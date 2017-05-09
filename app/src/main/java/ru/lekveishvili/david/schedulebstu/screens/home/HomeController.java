package ru.lekveishvili.david.schedulebstu.screens.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
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
        configureToolbar();
        configureRecycler();
        setModel();
    }

    private void setModel() {
        specificationsSectionAdapter.removeAllSections();
        specificationsSectionAdapter.addSection(new HomeSectionAdapter(createTestDay().getName(), createTestDay().getEventList()));
        specificationsSectionAdapter.addSection(new HomeSectionAdapter(createTestDay2().getName(), createTestDay2().getEventList()));
        specificationsSectionAdapter.addSection(new HomeSectionAdapter(createTestDay2().getName(), createTestDay2().getEventList()));
        specificationsSectionAdapter.notifyDataSetChanged();
    }

    private Day createTestDay() {
        return Day.newBuilder()
                .withName("среда, 10 мая")
                .withEventItems(createTestEvents())
                .build();
    }

    private Day createTestDay2() {
        return Day.newBuilder()
                .withName("четверг, 11 мая")
                .withEventItems(createTestEvents())
                .build();
    }

    private List<Event> createTestEvents() {
        List<Event> eventList = new ArrayList<>();
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
