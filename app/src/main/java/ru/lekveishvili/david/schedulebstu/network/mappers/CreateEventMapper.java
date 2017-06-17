package ru.lekveishvili.david.schedulebstu.network.mappers;

import ru.lekveishvili.david.schedulebstu.network.models.CreateEventResponse;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class CreateEventMapper {
    private MainApiService mainApiService;

    public CreateEventMapper(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public String transform(CreateEventResponse createEventResponse) throws Exception {
        return "Событие добавлено!";
    }
}
