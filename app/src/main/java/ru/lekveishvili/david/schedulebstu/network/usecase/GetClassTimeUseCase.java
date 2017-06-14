package ru.lekveishvili.david.schedulebstu.network.usecase;

import java.util.List;

import io.reactivex.Observable;
import ru.lekveishvili.david.schedulebstu.models.ClassTime;
import ru.lekveishvili.david.schedulebstu.network.mappers.ClassTimeMapper;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GetClassTimeUseCase {
    private final MainApiService mainApiService;
    private String token;

    public GetClassTimeUseCase(MainApiService mainApiService, String token) {
        this.mainApiService = mainApiService;
        this.token = token;
    }

    public Observable<List<ClassTime>> execute() {
        ClassTimeMapper mapper = new ClassTimeMapper(mainApiService);
        return mainApiService.getClassTime(token)
                .map(mapper::transform);
    }
}
