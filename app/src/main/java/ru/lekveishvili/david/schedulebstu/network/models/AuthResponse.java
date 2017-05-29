package ru.lekveishvili.david.schedulebstu.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuthResponse {

    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public Data data;

    public class Data {

        @SerializedName("name")
        public String name;
        @SerializedName("typeUser")
        public String typeUser;
        @SerializedName("groups")
        public List<String> groups = null;
        @SerializedName("token")
        public String token;

    }
}
