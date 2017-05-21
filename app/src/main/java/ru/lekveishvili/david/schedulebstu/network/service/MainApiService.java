package ru.lekveishvili.david.schedulebstu.network.service;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.lekveishvili.david.schedulebstu.network.models.EventTypesResponse;
import ru.lekveishvili.david.schedulebstu.network.models.EventWeekResponse;
import ru.lekveishvili.david.schedulebstu.network.models.GroupResponse;
import ru.lekveishvili.david.schedulebstu.network.models.RoomResponse;
import ru.lekveishvili.david.schedulebstu.network.models.SubjectResponse;
import ru.lekveishvili.david.schedulebstu.network.models.TeacherResponse;

public interface MainApiService {
    @GET("group/count/all")
    Observable<GroupResponse> getGroups();

    @GET("lectureHall/count/all")
    Observable<RoomResponse> getRooms();

    @GET("subject/count/all")
    Observable<SubjectResponse> getSubjects();

    @GET("lecture/count/all")
    Observable<TeacherResponse> getTeachers();

    @GET("eventType/count/all")
    Observable<EventTypesResponse> getEventTypes();

//    @GET("/event/week/{date}/group/{group}")
//    Observable<EventWeekResponse> getEventsWeekGroup(@Path("date") String date, @Path("group") String group);

    @GET("/event/week/2017-09-11/group/13-%D0%98%D0%92%D0%A21")
    Observable<EventWeekResponse> getEventsWeekGroup();


}