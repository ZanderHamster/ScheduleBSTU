package ru.lekveishvili.david.schedulebstu.network.service;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.lekveishvili.david.schedulebstu.network.models.AuthRequest;
import ru.lekveishvili.david.schedulebstu.network.models.AuthResponse;
import ru.lekveishvili.david.schedulebstu.network.models.EventTypesResponse;
import ru.lekveishvili.david.schedulebstu.network.models.EventWeekResponse;
import ru.lekveishvili.david.schedulebstu.network.models.GroupResponse;
import ru.lekveishvili.david.schedulebstu.network.models.RoomResponse;
import ru.lekveishvili.david.schedulebstu.network.models.SubjectResponse;
import ru.lekveishvili.david.schedulebstu.network.models.TeacherResponse;

public interface MainApiService {
    @POST("auth")
    Observable<AuthResponse> auth(@Header("X-Auth-Token") String token,
                                  @Body AuthRequest authRequest);

    @GET("group/count/all")
    Observable<GroupResponse> getGroups(@Header("X-Auth-Token") String token);

    @GET("lectureHall/count/all")
    Observable<RoomResponse> getRooms(@Header("X-Auth-Token") String token);

    @GET("subject/count/all")
    Observable<SubjectResponse> getSubjects(@Header("X-Auth-Token") String token);

    @GET("lecture/count/all")
    Observable<TeacherResponse> getTeachers(@Header("X-Auth-Token") String token);

    @GET("eventType/count/all")
    Observable<EventTypesResponse> getEventTypes(@Header("X-Auth-Token") String token);

//    @GET("/event/week/{date}/group/{group}")
//    Observable<EventWeekResponse> getEventsWeekGroup(@Path("date") String date, @Path("group") String group);

    //    @GET("/event/week/2017-09-11/group/13-%D0%98%D0%92%D0%A21")
    @GET("/event/week/{date}/group/{group}")
    Observable<EventWeekResponse> getEventsWeekGroup(@Path("date") String date,
                                                     @Path("group") String group);

    @GET("/event/week/{date}/lecture/{lecture}")
    Observable<EventWeekResponse> getEventsWeekLecture(@Path("date") String date,
                                                     @Path("lecture") String lecture);
}
