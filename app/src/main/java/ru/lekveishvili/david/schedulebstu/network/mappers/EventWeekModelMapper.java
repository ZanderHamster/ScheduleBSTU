package ru.lekveishvili.david.schedulebstu.network.mappers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public List<Event> transform(EventWeekResponse eventWeekResponse) {
        List<Event> result = new ArrayList<>();
        for (int i = 0; i < eventWeekResponse.timetable.size(); i++) {
            // Other
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
                            .build());
                }
                // Предмет
                Subject subject = Subject.newBuilder()
                        .withName(other.subject.name)
                        .withId(other.subject.id)
                        .build();

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
            // Basic
//            for (int j = 0; j < eventWeekResponse.timetable.get(i).basic._13201455.size(); j++) {
//                for (int k = 0; k < eventWeekResponse.timetable.get(i).basic._13201455.get(j).; k++) {
//
//                }
//
//            }

//            EventWeekResponse.Lecturer teacher = teacherResponse.lecturers.get(i);
//            String fullName = teacher.name;
//            String[] parts = fullName.split(" ");
//            result.add(Teacher.newBuilder()
//                    .withFirstName(parts[0])
//                    .withSecondName(parts[1])
//                    .withThirdName(parts[2])
//                    .withId(teacher.id)
//                    .build());
        }
        return result;
    }
}
