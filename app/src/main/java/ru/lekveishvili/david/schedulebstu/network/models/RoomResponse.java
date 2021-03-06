package ru.lekveishvili.david.schedulebstu.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoomResponse {
    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<LectureHall> data = null;

    public class LectureHall {

        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public String id;

    }

}
