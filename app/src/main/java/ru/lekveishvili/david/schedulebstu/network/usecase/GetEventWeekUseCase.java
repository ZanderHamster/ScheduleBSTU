package ru.lekveishvili.david.schedulebstu.network.usecase;

import java.util.List;

import io.reactivex.Observable;
import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.models.EventType;
import ru.lekveishvili.david.schedulebstu.network.mappers.EventTypeModelMapper;
import ru.lekveishvili.david.schedulebstu.network.mappers.EventWeekModelMapper;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GetEventWeekUseCase {
    private final MainApiService mainApiService;

    public GetEventWeekUseCase(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public Observable<List<Event>> execute() {
        EventWeekModelMapper mapper = new EventWeekModelMapper(mainApiService);
//        return mainApiService.getEventsWeekGroup("2017-09-11","13-%D0%98%D0%92%D0%A21")
        return mainApiService.getEventsWeekGroup()
                .map(mapper::transform);
    }
}
