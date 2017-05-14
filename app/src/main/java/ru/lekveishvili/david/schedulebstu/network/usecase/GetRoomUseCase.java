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

    public GetRoomUseCase(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public Observable<List<Room>> execute() {
        RoomModelMapper mapper = new RoomModelMapper(mainApiService);
        return mainApiService.getRooms()
                .map(mapper::transform);
    }
}
