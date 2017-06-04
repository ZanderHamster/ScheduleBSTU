package ru.lekveishvili.david.schedulebstu.network.usecase;


import io.reactivex.Observable;
import io.realm.RealmList;
import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.network.mappers.EventWeekModelMapper;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GetEventWeekUseCase {
    private final MainApiService mainApiService;
    private String token;

    public GetEventWeekUseCase(MainApiService mainApiService, String token) {
        this.mainApiService = mainApiService;
        this.token = token;
    }

    public Observable<RealmList<Event>> execute() {
        EventWeekModelMapper mapper = new EventWeekModelMapper(mainApiService);
//        return mainApiService.getEventsWeekGroup("2017-09-11","13-%D0%98%D0%92%D0%A21")
        return mainApiService.getEventsWeekGroup(token)
                .map(mapper::transform);
    }
}
