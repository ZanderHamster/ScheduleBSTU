package ru.lekveishvili.david.schedulebstu.network.mappers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.realm.RealmList;
import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.models.EventType;
import ru.lekveishvili.david.schedulebstu.models.Group;
import ru.lekveishvili.david.schedulebstu.models.Room;
import ru.lekveishvili.david.schedulebstu.models.Subject;
import ru.lekveishvili.david.schedulebstu.models.Teacher;
import ru.lekveishvili.david.schedulebstu.network.models.EventWeekResponse;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class EventWeekModelMapper {
    private MainApiService mainApiService;

    public EventWeekModelMapper(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }


    public RealmList<Event> transformBasic(EventWeekResponse eventWeekResponse) {
        RealmList<Event> result = new RealmList<>();
        if (eventWeekResponse.timetable != null) {
            for (int i = 0; i < eventWeekResponse.timetable.size(); i++) {
                for (int j = 0; j < eventWeekResponse.timetable.get(i).other.size(); j++) {
                    EventWeekResponse.Other other = eventWeekResponse.timetable.get(i).other.get(j);
                    // Дата и время события
                    String strDate = other.date.date; //"2017-09-11"
                    String start = other.classTime.time.start; //"13:20"
                    String end = other.classTime.time.end; //"15:05"
                    String strFullDateStart = strDate + " " + start;
                    String strFullDateEnd = strDate + " " + end;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("ru", "RU"));
                    Date dateStart = new Date();
                    Date dateEnd = new Date();
                    try {
                        dateStart = dateFormat.parse(strFullDateStart);
                        dateEnd = dateFormat.parse(strFullDateEnd);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // Тип события
                    EventType eventType = EventType.newBuilder()
                            .withName(other.eventType.name)
                            .withId(other.eventType.id)
                            .build();
                    // Группы принимающие участие в событии
                    RealmList<Group> groups = new RealmList<>();
                    for (int k = 0; k < other.groups.size(); k++) {
                        groups.add(Group.newBuilder()
                                .withId(other.groups.get(k).id)
                                .withName(other.groups.get(k).name)
                                .build());
                    }
                    // Аудитория
                    Room room = Room.newBuilder()
                            .withId(other.lectureHall.id)
                            .withName(other.lectureHall.name)
                            .build();
                    // Преподаватели принимающие участие в событии
                    RealmList<Teacher> teachers = new RealmList<>();
                    for (int k = 0; k < other.lecturer.size(); k++) {
                        String[] parts = other.lecturer.get(k).name.split(" ");
                        teachers.add(Teacher.newBuilder()
                                .withFirstName(parts[0])
                                .withSecondName(parts[1])
                                .withThirdName(parts[2])
                                .withId(other.lecturer.get(k).id)
                                .withFullName(other.lecturer.get(k).name)
                                .build());
                    }
                    // Предмет
                    Subject subject = Subject.newBuilder()
                            .withName(other.subject.name)
                            .withId(other.subject.id)
                            .build();

                    if (other.changeData == null
                            || other.changeData.changeText.equals("Событие перенесено на сегодня!")) {
                        result.add(Event.newBuilder()
                                .withId(other.id)
                                .withStartEvent(dateStart)
                                .withEndEvent(dateEnd)
                                .withEventType(eventType)
                                .withGroups(groups)
                                .withRoom(room)
                                .withTeachers(teachers)
                                .withSubject(subject)
                                .build());
                    }
                }

                // Basic
                for (int j = 0; j < eventWeekResponse.timetable.get(i).basic.size(); j++) {
                    boolean nextWeek = false;
                    EventWeekResponse.Basic basic = eventWeekResponse.timetable.get(i).basic.get(j);
                    //Дата события события
                    Integer dayOfTheWeek = basic.date.day + 1;
                    String strStartPeriod = basic.date.academicPeriod.date.start; // 2017-09-01
                    String strEndPeriod = basic.date.academicPeriod.date.end; // 2017-12-29
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("ru", "RU"));

                    Date dateStartPeriod = new Date();
                    Date dateEndPeriod = new Date();
                    try {
                        dateStartPeriod = dateFormat.parse(strStartPeriod);
                        dateEndPeriod = dateFormat.parse(strEndPeriod);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // Нахожу первую дату в семестре для текущей пары
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateStartPeriod);
                    int tmpDayOfTheWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
                    if (tmpDayOfTheWeek == 0) {
                        tmpDayOfTheWeek = 7;
                    }
                    while (!(dayOfTheWeek == tmpDayOfTheWeek)) {
                        cal.add(Calendar.DATE, 1);
                        if (tmpDayOfTheWeek == 7) {
                            nextWeek = true;
                        }
                        tmpDayOfTheWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
                        if (tmpDayOfTheWeek == 0) {
                            tmpDayOfTheWeek = 7;
                        }
                    }
                    if ((basic.date.drop == 1) && nextWeek || (basic.date.drop == 2) && !nextWeek) {
                        cal.add(Calendar.DATE, 0);
                    }
                    if ((basic.date.drop == 2) && nextWeek || (basic.date.drop == 1) && !nextWeek) {
                        cal.add(Calendar.DATE, 7);
                    }
                    // Тип события
                    EventType eventType = EventType.newBuilder()
                            .withName(basic.eventType.name)
                            .withId(basic.eventType.id)
                            .build();
                    // Группы принимающие участие в событии
                    RealmList<Group> groups = new RealmList<>();
                    for (int k = 0; k < basic.groups.size(); k++) {
                        groups.add(Group.newBuilder()
                                .withId(basic.groups.get(k).id)
                                .withName(basic.groups.get(k).name)
                                .build());
                    }
                    // Аудитория
                    Room room = Room.newBuilder()
                            .withId(basic.lectureHall.id)
                            .withName(basic.lectureHall.name)
                            .build();
                    // Преподаватели принимающие участие в событии
                    RealmList<Teacher> teachers = new RealmList<>();
                    for (int k = 0; k < basic.lecturer.size(); k++) {
                        String[] parts = basic.lecturer.get(k).name.split(" ");
                        // TODO убрать этот костыль
                        if (parts.length == 2) {
                            teachers.add(Teacher.newBuilder()
                                    .withFirstName(parts[0])
                                    .withSecondName(parts[1])
                                    .withFullName(basic.lecturer.get(k).name)
                                    .withId(basic.lecturer.get(k).id)
                                    .build());
                        } else {
                            teachers.add(Teacher.newBuilder()
                                    .withFirstName(parts[0])
                                    .withSecondName(parts[1])
                                    .withThirdName(parts[2])
                                    .withFullName(basic.lecturer.get(k).name)
                                    .withId(basic.lecturer.get(k).id)
                                    .build());
                        }
                    }
                    // Предмет
                    Subject subject = Subject.newBuilder()
                            .withName(basic.subject.name)
                            .withId(basic.subject.id)
                            .build();
                    //Время события события
                    String startEventTime = basic.classTime.time.start; //13:20
                    String[] parts1 = startEventTime.split(":");
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
                    startCalendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                    startCalendar.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
                    startCalendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(parts1[0]));
                    startCalendar.set(Calendar.MINUTE, Integer.valueOf(parts1[1]));
                    startCalendar.set(Calendar.SECOND, 0);
                    Date dateStart = startCalendar.getTime();

                    String endEventTime = basic.classTime.time.end; //15:05
                    String[] parts2 = endEventTime.split(":");
                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
                    endCalendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                    endCalendar.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
                    endCalendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(parts2[0]));
                    endCalendar.set(Calendar.MINUTE, Integer.valueOf(parts2[1]));
                    endCalendar.set(Calendar.SECOND, 0);
                    Date dateEnd = endCalendar.getTime();

                    // от calTime с каким то шагом до dateEndPeriod
                    while (cal.getTime().getTime() <= dateEndPeriod.getTime()) {
                        if (basic.changeData == null
                                || basic.changeData.changeText.equals("Событие перенесено на сегодня!")) {
                            result.add(Event.newBuilder()
                                    .withId(basic.id)
                                    .withStartEvent(dateStart)
                                    .withEndEvent(dateEnd)
                                    .withEventType(eventType)
                                    .withGroups(groups)
                                    .withRoom(room)
                                    .withTeachers(teachers)
                                    .withSubject(subject)
                                    .build());
                        }
                        if (basic.date.drop == 1 || basic.date.drop == 2) {
                            cal.add(Calendar.DATE, 14);
                            startCalendar.add(Calendar.DATE, 14);
                            endCalendar.add(Calendar.DATE, 14);
                        } else {
                            cal.add(Calendar.DATE, 7);
                            startCalendar.add(Calendar.DATE, 7);
                            endCalendar.add(Calendar.DATE, 7);
                        }

                        dateStart = startCalendar.getTime();
                        dateEnd = endCalendar.getTime();
                    }
                }
            }
        }
        return result;
    }
}
