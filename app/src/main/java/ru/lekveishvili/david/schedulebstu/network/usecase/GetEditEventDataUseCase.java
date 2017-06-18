package ru.lekveishvili.david.schedulebstu.network.usecase;

import io.reactivex.Observable;
import ru.lekveishvili.david.schedulebstu.network.mappers.CancelEventMapper;
import ru.lekveishvili.david.schedulebstu.network.models.CancelEventRequest;
import ru.lekveishvili.david.schedulebstu.network.models.EditEventRequest;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GetEditEventDataUseCase {
    private final MainApiService mainApiService;
    private String token;

    public GetEditEventDataUseCase(MainApiService mainApiService, String token) {
        this.mainApiService = mainApiService;
        this.token = token;
    }

    public Observable<Boolean> execute(EditEventRequest editEventRequest) {
        CancelEventMapper mapper = new CancelEventMapper(mainApiService);
        return mainApiService.editEvent(token, editEventRequest)
                .map(mapper::transform);
    }
}
