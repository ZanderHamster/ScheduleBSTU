package ru.lekveishvili.david.schedulebstu.network.mappers;

import java.util.ArrayList;
import java.util.List;

import ru.lekveishvili.david.schedulebstu.models.Subject;
import ru.lekveishvili.david.schedulebstu.network.models.SubjectResponse;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class SubjectModelMapper {
    private MainApiService mainApiService;

    public SubjectModelMapper(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public List<Subject> transform(SubjectResponse subjectResponse) {
        List<Subject> result = new ArrayList<>();
        if (subjectResponse.data != null) {
            for (int i = 0; i < subjectResponse.data.size(); i++) {
                SubjectResponse.Subject subject = subjectResponse.data.get(i);
                result.add(Subject.newBuilder()
                        .withName(subject.name)
                        .withId(subject.id)
                        .build());
            }
        }
        return result;
    }
}
