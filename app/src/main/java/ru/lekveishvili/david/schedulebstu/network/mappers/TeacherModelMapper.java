package ru.lekveishvili.david.schedulebstu.network.mappers;

import java.util.ArrayList;
import java.util.List;

import ru.lekveishvili.david.schedulebstu.models.Subject;
import ru.lekveishvili.david.schedulebstu.models.Teacher;
import ru.lekveishvili.david.schedulebstu.network.models.SubjectResponse;
import ru.lekveishvili.david.schedulebstu.network.models.TeacherResponse;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class TeacherModelMapper {
    private MainApiService mainApiService;

    public TeacherModelMapper(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public List<Teacher> transform(TeacherResponse teacherResponse) {
        List<Teacher> result = new ArrayList<>();

        for (int i = 0; i < teacherResponse.lecturers.size(); i++) {
            TeacherResponse.Lecturer teacher = teacherResponse.lecturers.get(i);
            String fullName = teacher.name;
            String[] parts = fullName.split(" ");
            result.add(Teacher.newBuilder()
                    .withFirstName(parts[0])
                    .withSecondName(parts[1])
                    .withThirdName(parts[2])
                    .withId(teacher.id)
                    .withFullName(fullName)
                    .build());
        }
        return result;
    }
}
