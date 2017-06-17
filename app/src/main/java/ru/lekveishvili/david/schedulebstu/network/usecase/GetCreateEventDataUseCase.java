package ru.lekveishvili.david.schedulebstu.network.usecase;

import io.reactivex.Observable;
import ru.lekveishvili.david.schedulebstu.network.mappers.CancelEventMapper;
import ru.lekveishvili.david.schedulebstu.network.mappers.CreateEventMapper;
import ru.lekveishvili.david.schedulebstu.network.models.CancelEventRequest;
import ru.lekveishvili.david.schedulebstu.network.models.CreateEventRequest;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GetCreateEventDataUseCase {
    private final MainApiService mainApiService;
    private String token;

    public GetCreateEventDataUseCase(MainApiService mainApiService, String token) {
        this.mainApiService = mainApiService;
        this.token = token;
    }

    public Observable<String> execute(CreateEventRequest createEventRequest) {
        CreateEventMapper mapper = new CreateEventMapper(mainApiService);
        return mainApiService.createEvent(token, createEventRequest)
                .map(mapper::transform);
    }
}
