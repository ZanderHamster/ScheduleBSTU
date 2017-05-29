package ru.lekveishvili.david.schedulebstu.network.mappers;

import java.util.ArrayList;
import java.util.List;

import ru.lekveishvili.david.schedulebstu.models.Room;
import ru.lekveishvili.david.schedulebstu.network.models.RoomResponse;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class RoomModelMapper {
    private MainApiService mainApiService;

    public RoomModelMapper(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public List<Room> transform(RoomResponse roomResponse) {
        List<Room> result = new ArrayList<>();
        if (roomResponse.data != null) {
            for (int i = 0; i < roomResponse.data.size(); i++) {
                RoomResponse.LectureHall lectureHall = roomResponse.data.get(i);
                result.add(Room.newBuilder()
                        .withName(lectureHall.name)
                        .withId(lectureHall.id)
                        .build());
            }
        }
        return result;
    }
}
