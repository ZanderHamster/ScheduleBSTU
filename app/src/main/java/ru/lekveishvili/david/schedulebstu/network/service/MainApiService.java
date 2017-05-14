package ru.lekveishvili.david.schedulebstu.network.service;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.lekveishvili.david.schedulebstu.network.models.GroupResponse;
import ru.lekveishvili.david.schedulebstu.network.models.RoomResponse;

public interface MainApiService {
    @GET("group/count/all")
    Observable<GroupResponse> getGroups();

    @GET("lectureHall/count/all")
    Observable<RoomResponse> getRooms();


}
