package ru.lekveishvili.david.schedulebstu.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventTypesResponse {

    @SerializedName("message")
    public String message;
    @SerializedName("eventTypes")
    public List<EventType> eventTypes = null;

    public class EventType {

        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public String id;

    }
}
