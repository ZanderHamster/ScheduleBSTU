package ru.lekveishvili.david.schedulebstu.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubjectResponse {

    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public List<Subject> data = null;

    public class Subject {

        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public String id;

    }
}
