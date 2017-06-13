package ru.lekveishvili.david.schedulebstu.network.mappers;

import ru.lekveishvili.david.schedulebstu.network.models.CancelEventResponse;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class CancelEventMapper {
    private MainApiService mainApiService;

    public CancelEventMapper(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public boolean transform(CancelEventResponse cancelEventResponse) {

        return true;
    }
}
