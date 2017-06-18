package ru.lekveishvili.david.schedulebstu.screens.create_event;


import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.SessionService;
import ru.lekveishvili.david.schedulebstu.models.Authorization;
import ru.lekveishvili.david.schedulebstu.models.ClassTime;
import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.models.EventType;
import ru.lekveishvili.david.schedulebstu.models.Group;
import ru.lekveishvili.david.schedulebstu.models.Room;
import ru.lekveishvili.david.schedulebstu.models.Subject;
import ru.lekveishvili.david.schedulebstu.models.Teacher;
import ru.lekveishvili.david.schedulebstu.network.RetrofitClient;
import ru.lekveishvili.david.schedulebstu.network.models.CreateEventRequest;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;
import ru.lekveishvili.david.schedulebstu.network.usecase.GetCreateEventDataUseCase;
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

    @BindView(R.id.create_event_date_hint)
    LinearLayout layoutDate;
    @BindView(R.id.create_event_date_hint_image)
    ImageView hintImageDate;
    @BindView(R.id.create_event_date_hint_text)
    TextView tvDate;
    @BindView(R.id.button_date_event_reset)
    ImageView resetDate;

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
        configureDate();

        btnCreateEvent.setOnClickListener(v -> {
            if (spinnerStartEvent.getSelectedItemPosition() != 0
                    && spinnerEventType.getSelectedItemPosition() != 0
                    && spinnerRoom.getSelectedItemPosition() != 0
                    && spinnerGroup.getSelectedItemPosition() != 0
                    && spinnerSubject.getSelectedItemPosition() != 0
                    && !tvDate.getText().equals("Выбрать дату")) {
                MainApiService apiService = RetrofitClient.getMainApiService();

                String teacherName = "";
                String token = "";
                RealmResults<Authorization> accounts = realm.where(Authorization.class).findAll();
                if (accounts.size() != 0) {
                    teacherName = accounts.get(0).getFullName();
                    token = accounts.get(0).getToken();
                }
                RealmResults<Teacher> fullName = realm.where(Teacher.class)
                        .equalTo("fullName", teacherName)
                        .findAll();
                List<String> lecturers = new ArrayList<>();
                if (fullName.size() != 0) {
                    lecturers.add(fullName.get(0).getId());
                }

                Date startEvent = newEvent.getStartEvent();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", new Locale("ru", "RU"));
                String date = df.format(startEvent);


                CreateEventRequest createEventRequest = new CreateEventRequest();
                createEventRequest.subject = new CreateEventRequest.Subject();
                createEventRequest.groups = new ArrayList<>();
                createEventRequest.lectureHall = new CreateEventRequest.LectureHall();
                createEventRequest.eventType = new CreateEventRequest.EventType();
                createEventRequest.classTime = new CreateEventRequest.ClassTime();
                createEventRequest.lecturer = new ArrayList<>();
                createEventRequest.date = new CreateEventRequest.Date();


                CreateEventRequest.Group group = new CreateEventRequest.Group();
                group.id = newEvent.getGroups().get(0).getId();
                CreateEventRequest.Lecturer lecturer = new CreateEventRequest.Lecturer();
                lecturer.id = lecturers.get(0);

                createEventRequest.subject.id = newEvent.getSubject().getId();
                createEventRequest.groups.add(group);
                createEventRequest.lectureHall.id = newEvent.getRoom().getId();
                createEventRequest.eventType.id = newEvent.getEventType().getId();
                createEventRequest.classTime.id = newEvent.getTimeId();
                createEventRequest.lecturer.add(lecturer);
                createEventRequest.date.date = date;


                new GetCreateEventDataUseCase(apiService, token).execute(createEventRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::createEventMessage,
                                throwable -> Snackbar.make(btnCreateEvent, "Невозможно создать событие!",
                                        Snackbar.LENGTH_SHORT).show()
                        );
            }
        });


    }

    private void createEventMessage(String msg) {
        spinnerSubject.setSelection(0);
        setVisibilityResetSubject(false);
        spinnerGroup.setSelection(0);
        setVisibilityResetGroup(false);
        spinnerRoom.setSelection(0);
        setVisibilityResetRoom(false);
        spinnerEventType.setSelection(0);
        setVisibilityResetEventType(false);
        spinnerStartEvent.setSelection(0);
        setVisibilityResetStartEvent(false);
        tvDate.setTextColor(getResources().getColor(R.color.hint_color));
        tvDate.setText("Выбрать дату");
        setVisibilityResetDate(false);
        newEvent = Event.newBuilder().build();

        Snackbar.make(btnCreateEvent, msg,
                Snackbar.LENGTH_SHORT).show();
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
                    cal.set(Calendar.SECOND, 0);

                    String[] splitEnd = strEnd.split(":");
                    Calendar calEnd = Calendar.getInstance();
                    calEnd.set(Calendar.HOUR_OF_DAY, Integer.valueOf(splitEnd[0]));
                    calEnd.set(Calendar.MINUTE, Integer.valueOf(splitEnd[1]));
                    calEnd.set(Calendar.SECOND, 0);

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

    private void configureDate() {
        layoutDate.setOnClickListener(v -> {
            Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", new Locale("ru", "RU"));
                tvDate.setText(sdf.format(myCalendar.getTime()));
                tvDate.setTextColor(getResources().getColor(R.color.black));
                setVisibilityResetDate(true);

                Calendar calStart = Calendar.getInstance();
                if (newEvent.getStartEvent() != null) {
                    calStart.setTime(newEvent.getStartEvent());
                }
                calStart.set(Calendar.YEAR, year);
                calStart.set(Calendar.MONTH, monthOfYear);
                calStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Calendar calEnd = Calendar.getInstance();
                if (newEvent.getEndEvent() != null) {
                    calEnd.setTime(newEvent.getEndEvent());
                }
                calEnd.set(Calendar.YEAR, year);
                calEnd.set(Calendar.MONTH, monthOfYear);
                calEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                newEvent = Event.newBuilder(newEvent)
                        .withStartEvent(calStart.getTime())
                        .withEndEvent(calEnd.getTime())
                        .build();
            };
            new DatePickerDialog(v.getContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        resetDate.setOnClickListener(v -> {
            tvDate.setTextColor(getResources().getColor(R.color.hint_color));
            tvDate.setText("Выбрать дату");
            setVisibilityResetDate(false);
        });
    }

    private void setVisibilityResetDate(boolean flag) {
        if (flag) {
            resetDate.setVisibility(View.VISIBLE);
            hintImageDate.setVisibility(View.GONE);
        } else {
            resetDate.setVisibility(View.GONE);
            hintImageDate.setVisibility(View.VISIBLE);
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
