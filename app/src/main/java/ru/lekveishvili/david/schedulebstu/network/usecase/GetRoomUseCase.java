package ru.lekveishvili.david.schedulebstu.network.usecase;

import java.util.List;

import io.reactivex.Observable;
import ru.lekveishvili.david.schedulebstu.models.Group;
import ru.lekveishvili.david.schedulebstu.models.Room;
import ru.lekveishvili.david.schedulebstu.network.mappers.GroupModelMapper;
import ru.lekveishvili.david.schedulebstu.network.mappers.RoomModelMapper;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GetRoomUseCase {
    private final MainApiService mainApiService;
    private String token;

    public GetRoomUseCase(MainApiService mainApiService, String token) {
        this.mainApiService = mainApiService;
        this.token = token;
    }

    public Observable<List<Room>> execute() {
        RoomModelMapper mapper = new RoomModelMapper(mainApiService);
        return mainApiService.getRooms(token)
                .map(mapper::transform);
    }
}
