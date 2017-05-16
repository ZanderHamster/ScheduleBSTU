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

    public GetEventTypeUseCase(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public Observable<List<EventType>> execute() {
        EventTypeModelMapper mapper = new EventTypeModelMapper(mainApiService);
        return mainApiService.getEventTypes()
                .map(mapper::transform);
    }
}
