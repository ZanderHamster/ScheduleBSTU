package ru.lekveishvili.david.schedulebstu.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeacherResponse {

    @SerializedName("message")
    public String message;
    @SerializedName("lecturers")
    public List<Lecturer> lecturers = null;


    public class Lecturer {

        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public String id;

    }
}
