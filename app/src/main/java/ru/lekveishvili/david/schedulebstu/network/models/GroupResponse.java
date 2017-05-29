package ru.lekveishvili.david.schedulebstu.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupResponse {

    @SerializedName("message")
    public String message;
    @SerializedName("groups")
    public List<Group> data = null;

    public static class Group {

        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public String id;
    }
}
