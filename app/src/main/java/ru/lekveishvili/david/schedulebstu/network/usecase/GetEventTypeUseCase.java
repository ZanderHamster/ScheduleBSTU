package ru.lekveishvili.david.schedulebstu.network.usecase;

import java.util.List;

import io.reactivex.Observable;
import ru.lekveishvili.david.schedulebstu.models.EventType;
import ru.lekveishvili.david.schedulebstu.models.Group;
import ru.lekveishvili.david.schedulebstu.network.mappers.EventTypeModelMapper;
import ru.lekveishvili.david.schedulebstu.network.mappers.GroupModelMapper;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GetEventTypeUseCase {
    private final MainApiService mainApiService;
    private String token;

    public GetEventTypeUseCase(MainApiService mainApiService, String token) {
        this.mainApiService = mainApiService;
        this.token = token;
    }

    public Observable<List<EventType>> execute() {
        EventTypeModelMapper mapper = new EventTypeModelMapper(mainApiService);
        return mainApiService.getEventTypes(token)
                .map(mapper::transform);
    }
}
