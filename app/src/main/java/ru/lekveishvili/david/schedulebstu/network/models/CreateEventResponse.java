package ru.lekveishvili.david.schedulebstu.network.models;

import com.google.gson.annotations.SerializedName;

public class CreateEventResponse {

    @SerializedName("message")
    public String message;
    @SerializedName("errors")
    public Errors errors;

    public class Errors {
        @SerializedName("errors")
        public Errors_ errors;
    }

    public class Errors_ {
        @SerializedName("lecturer")
        public String lecturer;
        @SerializedName("lectureHall")
        public String lectureHall;
        @SerializedName("groups")
        public String groups;

    }
}
