package ru.lekveishvili.david.schedulebstu.network.usecase;

import java.util.List;

import io.reactivex.Observable;
import ru.lekveishvili.david.schedulebstu.models.Group;
import ru.lekveishvili.david.schedulebstu.network.mappers.GroupModelMapper;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GetGroupUseCase {
    private final MainApiService mainApiService;
    private String token;

    public GetGroupUseCase(MainApiService mainApiService, String token) {
        this.mainApiService = mainApiService;
        this.token = token;
    }

    public Observable<List<Group>> execute() {
        GroupModelMapper mapper = new GroupModelMapper(mainApiService);
        return mainApiService.getGroups(token)
                .map(mapper::transform);
    }
}
