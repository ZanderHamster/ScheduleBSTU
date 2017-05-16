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

    public GetSubjectUseCase(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public Observable<List<Subject>> execute() {
        SubjectModelMapper mapper = new SubjectModelMapper(mainApiService);
        return mainApiService.getSubjects()
                .map(mapper::transform);
    }
}
