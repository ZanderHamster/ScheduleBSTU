package ru.lekveishvili.david.schedulebstu.network.usecase;

import io.reactivex.Observable;
import ru.lekveishvili.david.schedulebstu.network.mappers.CancelEventMapper;
import ru.lekveishvili.david.schedulebstu.network.models.CancelEventRequest;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GetDeleteEventDataUseCase {
    private final MainApiService mainApiService;
    private String token;

    public GetDeleteEventDataUseCase(MainApiService mainApiService, String token) {
        this.mainApiService = mainApiService;
        this.token = token;
    }

    public Observable<Boolean> execute(CancelEventRequest cancelEventRequest) {
        CancelEventMapper mapper = new CancelEventMapper(mainApiService);
        return mainApiService.cancelEvent(token, cancelEventRequest)
                .map(mapper::transform);
    }
}
