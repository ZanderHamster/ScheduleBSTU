package ru.lekveishvili.david.schedulebstu.network.usecase;

import java.util.List;

import io.reactivex.Observable;
import ru.lekveishvili.david.schedulebstu.models.Authorization;
import ru.lekveishvili.david.schedulebstu.network.mappers.AuthMapper;
import ru.lekveishvili.david.schedulebstu.network.models.AuthRequest;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GetAuthorizationUseCase {
    private final MainApiService mainApiService;

    public GetAuthorizationUseCase(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public Observable<Authorization> execute(AuthRequest authRequest) {
        AuthMapper mapper = new AuthMapper(mainApiService);
        return mainApiService.auth(authRequest)
                .map(mapper::transform);
    }
}
