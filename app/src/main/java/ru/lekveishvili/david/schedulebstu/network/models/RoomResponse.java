package ru.lekveishvili.david.schedulebstu.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoomResponse {
    @SerializedName("message")
    public String message;

    @SerializedName("lectureHalls")
    public List<LectureHall> lectureHalls = null;

    public class LectureHall {

        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public String id;

    }

}
