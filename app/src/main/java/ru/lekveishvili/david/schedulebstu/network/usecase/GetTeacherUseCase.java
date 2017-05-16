package ru.lekveishvili.david.schedulebstu.network.usecase;

import java.util.List;

import io.reactivex.Observable;
import ru.lekveishvili.david.schedulebstu.models.Subject;
import ru.lekveishvili.david.schedulebstu.models.Teacher;
import ru.lekveishvili.david.schedulebstu.network.mappers.SubjectModelMapper;
import ru.lekveishvili.david.schedulebstu.network.mappers.TeacherModelMapper;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GetTeacherUseCase {
    private final MainApiService mainApiService;

    public GetTeacherUseCase(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public Observable<List<Teacher>> execute() {
        TeacherModelMapper mapper = new TeacherModelMapper(mainApiService);
        return mainApiService.getTeachers()
                .map(mapper::transform);
    }
}
