package ru.lekveishvili.david.schedulebstu.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateEventRequest {
    @SerializedName("subject")
    public Subject subject;
    @SerializedName("lecturer")
    public List<Lecturer> lecturer = null;
    @SerializedName("lectureHall")
    public LectureHall lectureHall;
    @SerializedName("groups")
    public List<Group> groups = null;
    @SerializedName("eventType")
    public EventType eventType;
    @SerializedName("classTime")
    public ClassTime classTime;
    @SerializedName("date")
    public Date date;

    public static class EventType {
        @SerializedName("id")
        public String id;
    }

    public static class Date {
        @SerializedName("date")
        public String date;
    }

    public static class ClassTime {
        @SerializedName("id")
        public String id;
    }

    public static class Group {
        @SerializedName("id")
        public String id;
    }

    public static class LectureHall {
        @SerializedName("id")
        public String id;
    }

    public static class Lecturer {
        @SerializedName("id")
        public String id;
    }

    public static class Subject {
        @SerializedName("id")
        public String id;
    }
}
