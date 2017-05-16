package ru.lekveishvili.david.schedulebstu.network.mappers;

import java.util.ArrayList;
import java.util.List;

import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.models.Teacher;
import ru.lekveishvili.david.schedulebstu.network.models.EventWeekResponse;
import ru.lekveishvili.david.schedulebstu.network.models.TeacherResponse;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class EventModelMapper {
    private MainApiService mainApiService;

    public EventModelMapper(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public List<Event> transform(EventWeekResponse eventWeekResponse) {
        List<Event> result = new ArrayList<>();

        for (int i = 0; i < eventWeekResponse.timetable.size(); i++) {
            for (int j = 0; j < eventWeekResponse.timetable.get(i).other.size(); j++) {
                EventWeekResponse.Other other = eventWeekResponse.timetable.get(i).other.get(j);
                String date = other.date.date; //"2017-09-11"
                String day = other.date.day; // 0 - № дня в неделе
                other.classTime.time
            }
            for (int j = 0; j < eventWeekResponse.timetable.get(i).basic._13201455.size(); j++) {
                for (int k = 0; k < eventWeekResponse.timetable.get(i).basic._13201455.get(j).; k++) {

                }
                
            }
            EventWeekResponse.Lecturer teacher = teacherResponse.lecturers.get(i);
            String fullName = teacher.name;
            String[] parts = fullName.split(" ");
            result.add(Teacher.newBuilder()
                    .withFirstName(parts[0])
                    .withSecondName(parts[1])
                    .withThirdName(parts[2])
                    .withId(teacher.id)
                    .build());
        }
        return result;
    }
}
