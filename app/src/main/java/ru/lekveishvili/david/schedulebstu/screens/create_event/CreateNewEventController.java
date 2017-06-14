package ru.lekveishvili.david.schedulebstu.screens.create_event;


import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.models.ClassTime;
import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.models.EventType;
import ru.lekveishvili.david.schedulebstu.models.Group;
import ru.lekveishvili.david.schedulebstu.models.Room;
import ru.lekveishvili.david.schedulebstu.models.Subject;
import ru.lekveishvili.david.schedulebstu.models.Teacher;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;
import ru.lekveishvili.david.schedulebstu.screens.search.adapters.NothingSelectedSpinnerAdapter;

public class CreateNewEventController extends BaseController {
    private Realm realm;
    private Event newEvent = Event.newBuilder().build();
    @BindView(R.id.new_event_toolbar_back_button)
    ImageView btnBack;
    @BindView(R.id.new_event_toolbar_title)
    TextView toolbarTitle;

    @BindView(R.id.spinner_select_subject)
    Spinner spinnerSubject;
    @BindView(R.id.spinner_select_subject_reset)
    ImageView resetSubject;
    @BindView(R.id.spinner_select_group)
    Spinner spinnerGroup;
    @BindView(R.id.spinner_select_group_reset)
    ImageView resetGroup;
    @BindView(R.id.spinner_select_room)
    Spinner spinnerRoom;
    @BindView(R.id.spinner_select_room_reset)
    ImageView resetRoom;
    @BindView(R.id.spinner_select_event_type)
    Spinner spinnerEventType;
    @BindView(R.id.spinner_select_event_type_reset)
    ImageView resetEventType;
    @BindView(R.id.spinner_select_start_event)
    Spinner spinnerStartEvent;
    @BindView(R.id.spinner_select_start_event_reset)
    ImageView resetStartEvent;

    @BindView(R.id.create_event)
    Button btnCreateEvent;


    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        realm = Realm.getDefaultInstance();
        configureToolbar();

        configureSpinnerSubject();
        configureSpinnerGroup();
        configureSpinnerRoom();
        configureSpinnerEventType();
        configureSpinnerStartEvent();
        btnCreateEvent.setOnClickListener(v -> {
            Event build = Event.newBuilder(newEvent).build();
            int size1 = build.getGroups().size();
            List<Group> groups = newEvent.getGroups();
            List<Teacher> teachers = newEvent.getTeachers();
            String name = newEvent.getEventType().getName();
            String id = newEvent.getEventType().getId();
            String name1 = newEvent.getRoom().getName();
            String id1 = newEvent.getRoom().getId();
            String id2 = newEvent.getSubject().getId();
            String name2 = newEvent.getSubject().getName();
            Date startEvent = newEvent.getStartEvent();
            Date endEvent = newEvent.getEndEvent();
            int size = groups.size();
        });

    }

    private void configureSpinnerSubject() {
        List<String> subjectList = new ArrayList<>();
        RealmResults<Subject> subjects = realm.where(Subject.class)
                .findAll();
        for (int i = 0; i < subjects.size(); i++) {
            if (!subjectList.contains(subjects.get(i).getName())) {
                subjectList.add(subjects.get(i).getName());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                subjectList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter,
                        R.layout.item_spinner_row_nothing_selected_subject,
                        getActivity()));
        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    setVisibilityResetSubject(true);
                    String strId = "";
                    for (int i = 0; i < subjects.size(); i++) {
                        if (subjectList.get(position - 1).equals(subjects.get(i).getName())) {
                            strId = subjects.get(i).getId();
                        }
                    }
                    newEvent = Event.newBuilder(newEvent)
                            .withSubject(Subject.newBuilder()
                                    .withName(subjectList.get(position - 1))
                                    .withId(strId)
                                    .build())
                            .build();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        resetSubject.setOnClickListener(v -> {
            spinnerSubject.setSelection(0);
            setVisibilityResetSubject(false);
        });
    }

    private void setVisibilityResetSubject(boolean flag) {
        if (flag) {
            resetSubject.setVisibility(View.VISIBLE);
        } else {
            resetSubject.setVisibility(View.GONE);
        }
    }

    private void configureSpinnerGroup() {
        List<String> groupList = new ArrayList<>();
        RealmResults<Group> groups = realm.where(Group.class)
                .findAll();
        for (int i = 0; i < groups.size(); i++) {
            if (!groupList.contains(groups.get(i).getName())) {
                groupList.add(groups.get(i).getName());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                groupList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter,
                        R.layout.item_spinner_row_nothing_selected_groups,
                        getActivity()));
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    setVisibilityResetGroup(true);
                    String strId = "";
                    for (int i = 0; i < groups.size(); i++) {
                        if (groupList.get(position - 1).equals(groups.get(i).getName())) {
                            strId = groups.get(i).getId();
                        }
                    }
                    RealmList<Group> realmGroups = new RealmList<>();
                    realmGroups.add(Group.newBuilder()
                            .withName(groupList.get(position - 1))
                            .withId(strId)
                            .build());
                    newEvent = Event.newBuilder(newEvent)
                            .withGroups(realmGroups)
                            .build();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        resetGroup.setOnClickListener(v -> {
            spinnerGroup.setSelection(0);
            setVisibilityResetGroup(false);
        });
    }

    private void setVisibilityResetGroup(boolean flag) {
        if (flag) {
            resetGroup.setVisibility(View.VISIBLE);
        } else {
            resetGroup.setVisibility(View.GONE);
        }
    }

    private void configureSpinnerRoom() {
        List<String> rooms = new ArrayList<>();
        RealmResults<Room> room = realm.where(Room.class)
                .findAll();
        for (int i = 0; i < room.size(); i++) {
            if (!rooms.contains(room.get(i).getName())) {
                rooms.add(room.get(i).getName());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                rooms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoom.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter,
                        R.layout.item_spinner_row_nothing_selected_room,
                        getActivity()));
        spinnerRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    setVisibilityResetRoom(true);
                    String strId = "";
                    for (int i = 0; i < room.size(); i++) {
                        if (rooms.get(position - 1).equals(room.get(i).getName())) {
                            strId = room.get(i).getId();
                        }
                    }
                    newEvent = Event.newBuilder(newEvent)
                            .withRoom(Room.newBuilder()
                                    .withName(rooms.get(position - 1))
                                    .withId(strId)
                                    .build())
                            .build();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        resetRoom.setOnClickListener(v -> {
            spinnerRoom.setSelection(0);
            setVisibilityResetRoom(false);
        });
    }

    private void setVisibilityResetRoom(boolean flag) {
        if (flag) {
            resetRoom.setVisibility(View.VISIBLE);
        } else {
            resetRoom.setVisibility(View.GONE);
        }
    }

    private void configureSpinnerEventType() {
        List<String> eventTypes = new ArrayList<>();
        RealmResults<EventType> eventType = realm.where(EventType.class)
                .findAll();
        for (int i = 0; i < eventType.size(); i++) {
            if (!eventTypes.contains(eventType.get(i).getName())) {
                eventTypes.add(eventType.get(i).getName());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                eventTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEventType.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter,
                        R.layout.item_spinner_row_nothing_selected_event_type,
                        getActivity()));
        spinnerEventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    setVisibilityResetEventType(true);
                    String strId = "";
                    for (int i = 0; i < eventType.size(); i++) {
                        if (eventTypes.get(position - 1).equals(eventType.get(i).getName())) {
                            strId = eventType.get(i).getId();
                        }
                    }
                    newEvent = Event.newBuilder(newEvent)
                            .withEventType(EventType.newBuilder()
                                    .withName(eventTypes.get(position - 1))
                                    .withId(strId)
                                    .build())
                            .build();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        resetEventType.setOnClickListener(v -> {
            spinnerEventType.setSelection(0);
            setVisibilityResetEventType(false);
        });
    }

    private void setVisibilityResetEventType(boolean flag) {
        if (flag) {
            resetEventType.setVisibility(View.VISIBLE);
        } else {
            resetEventType.setVisibility(View.GONE);
        }
    }

    private void configureSpinnerStartEvent() {
        List<String> startEvent = new ArrayList<>();
        RealmResults<ClassTime> classTimes = realm.where(ClassTime.class)
                .findAll();
        for (int i = 0; i < classTimes.size(); i++) {
            if (!startEvent.contains(classTimes.get(i).getStart())) {
                startEvent.add(classTimes.get(i).getStart());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                startEvent);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartEvent.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter,
                        R.layout.item_spinner_row_nothing_selected_start_event,
                        getActivity()));
        spinnerStartEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    setVisibilityResetStartEvent(true);
                    String strId = "";
                    String strStart = "";
                    String strEnd = "";
                    for (int i = 0; i < classTimes.size(); i++) {
                        if (startEvent.get(position - 1).equals(classTimes.get(i).getStart())) {
                            strId = classTimes.get(i).getId();
                            strStart = classTimes.get(i).getStart();
                            strEnd = classTimes.get(i).getEnd();
                        }
                    }
                    String[] splitStart = strStart.split(":");
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(splitStart[0]));
                    cal.set(Calendar.MINUTE, Integer.valueOf(splitStart[1]));
                    cal.set(Calendar.SECOND,0);
                    cal.getTime();

                    String[] splitEnd = strEnd.split(":");
                    Calendar calEnd = Calendar.getInstance();
                    calEnd.set(Calendar.HOUR_OF_DAY, Integer.valueOf(splitEnd[0]));
                    calEnd.set(Calendar.MINUTE, Integer.valueOf(splitEnd[1]));
                    calEnd.set(Calendar.SECOND,0);
                    calEnd.getTime();

                    newEvent = Event.newBuilder(newEvent)
                            .withStartEvent(cal.getTime())
                            .withEndEvent(calEnd.getTime())
                            .withTimeId(strId)
                            .build();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        resetStartEvent.setOnClickListener(v -> {
            spinnerStartEvent.setSelection(0);
            setVisibilityResetStartEvent(false);
        });
    }

    private void setVisibilityResetStartEvent(boolean flag) {
        if (flag) {
            resetStartEvent.setVisibility(View.VISIBLE);
        } else {
            resetStartEvent.setVisibility(View.GONE);
        }
    }


    private void configureToolbar() {
        btnBack.setOnClickListener(v -> getRouter().handleBack());
        toolbarTitle.setText(getResources().getString(R.string.create_new_event));
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_create_new_event, container, false);
    }
}
