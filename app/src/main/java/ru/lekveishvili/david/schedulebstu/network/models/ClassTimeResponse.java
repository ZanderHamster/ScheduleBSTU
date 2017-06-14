package ru.lekveishvili.david.schedulebstu.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassTimeResponse {

    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public List<Datum> data = null;

    public class Time {

        @SerializedName("start")
        public String start;
        @SerializedName("end")
        public String end;

    }

    public class Datum {

        @SerializedName("name")
        public String name;
        @SerializedName("time")
        public Time time;
        @SerializedName("id")
        public String id;

    }
}
