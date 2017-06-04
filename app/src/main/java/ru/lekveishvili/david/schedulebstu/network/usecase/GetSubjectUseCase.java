package ru.lekveishvili.david.schedulebstu.network.usecase;

import java.util.List;

import io.reactivex.Observable;
import ru.lekveishvili.david.schedulebstu.models.Room;
import ru.lekveishvili.david.schedulebstu.models.Subject;
import ru.lekveishvili.david.schedulebstu.network.mappers.RoomModelMapper;
import ru.lekveishvili.david.schedulebstu.network.mappers.SubjectModelMapper;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GetSubjectUseCase {
    private final MainApiService mainApiService;
    private String token;

    public GetSubjectUseCase(MainApiService mainApiService, String token) {
        this.mainApiService = mainApiService;
        this.token = token;
    }

    public Observable<List<Subject>> execute() {
        SubjectModelMapper mapper = new SubjectModelMapper(mainApiService);
        return mainApiService.getSubjects(token)
                .map(mapper::transform);
    }
}
