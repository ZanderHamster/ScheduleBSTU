package ru.lekveishvili.david.schedulebstu.network.usecase;

import io.reactivex.Observable;
import ru.lekveishvili.david.schedulebstu.models.Authorization;
import ru.lekveishvili.david.schedulebstu.network.mappers.AuthMapper;
import ru.lekveishvili.david.schedulebstu.network.models.AuthRequest;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GetAuthorizationUseCase {
    private final MainApiService mainApiService;
    private String token;

    public GetAuthorizationUseCase(MainApiService mainApiService, String token) {
        this.mainApiService = mainApiService;
        this.token = token;
    }

    public Observable<Authorization> execute(AuthRequest authRequest) {
        AuthMapper mapper = new AuthMapper(mainApiService);
        return mainApiService.auth(token, authRequest)
                .map(mapper::transform);
    }
}
