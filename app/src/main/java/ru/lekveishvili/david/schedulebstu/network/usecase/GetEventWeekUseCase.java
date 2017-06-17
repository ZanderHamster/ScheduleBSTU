package ru.lekveishvili.david.schedulebstu.network.usecase;


import io.reactivex.Observable;
import io.realm.RealmList;
import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.network.mappers.EventWeekModelMapper;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GetEventWeekUseCase {
    private final MainApiService mainApiService;
    private String token;
    private String date;
    private String groupOrLecture;


    public GetEventWeekUseCase(MainApiService mainApiService, String token, String date, String groupOrLecture) {
        this.mainApiService = mainApiService;
        this.token = token;
        this.date = date;
        this.groupOrLecture = groupOrLecture;
    }

    public Observable<RealmList<Event>> executeGroup() {
        EventWeekModelMapper mapper = new EventWeekModelMapper(mainApiService);
        return mainApiService.getEventsWeekGroup(date, groupOrLecture)
                .map(mapper::transformBasic);
    }

    public Observable<RealmList<Event>> executeOtherGroup() {
        EventWeekModelMapper mapper = new EventWeekModelMapper(mainApiService);
        return mainApiService.getEventsWeekGroup(date, groupOrLecture)
                .map(mapper::transformOther);
    }

    public Observable<RealmList<Event>> executeTeacher() {
        EventWeekModelMapper mapper = new EventWeekModelMapper(mainApiService);
        return mainApiService.getEventsWeekLecture(date, groupOrLecture)
                .map(mapper::transformBasic);
    }
}
