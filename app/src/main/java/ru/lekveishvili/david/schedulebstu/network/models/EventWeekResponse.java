package ru.lekveishvili.david.schedulebstu.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventWeekResponse {
    @SerializedName("message")
    public String message;
    @SerializedName("timetable")
    public List<Timetable> timetable = null;

    public class Timetable {
        @SerializedName("basic")
        public Basic basic;
        @SerializedName("other")
        public List<Other> other = null;
    }
    public class Basic {
        @SerializedName("13:20-14:55")
        public List<_13201455> _13201455 = null;
        @SerializedName("15:05-16:40")
        public List<_15051640> _15051640 = null;
        @SerializedName("16:50-17:25")
        public List<_16501725> _16501725 = null;
    }
    public class Other {
        @SerializedName("date")
        public DateOther date;
        @SerializedName("classTime")
        public ClassTime classTime;
        @SerializedName("eventType")
        public EventType eventType;
        @SerializedName("groups")
        public List<Group> groups = null;
        @SerializedName("lectureHall")
        public LectureHall lectureHall;
        @SerializedName("lecturer")
        public List<Lecturer> lecturer = null;
        @SerializedName("subject")
        public Subject subject;
        @SerializedName("id")
        public String id;

    }
    public class AcademicPeriod {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("date")
        public Date_ date;

    }
    public class ClassTime {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("time")
        public Time time;
    }
    public class ClassTimeOther {
        @SerializedName("name")
        public String name;
        @SerializedName("time")
        public Time time;
    }
    public class DateOther{
        @SerializedName("date")
        public String date;
        @SerializedName("day")
        public String day;
    }
    public class Date_{
        @SerializedName("start")
        public String start;
        @SerializedName("end")
        public String end;
    }
    public class Date {
        @SerializedName("drop")
        public Integer drop;
        @SerializedName("day")
        public Integer day;
        @SerializedName("academicPeriod")
        public AcademicPeriod academicPeriod;
    }
    public class EventType {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
    }
    public class EventTypeOther {
        @SerializedName("name")
        public String name;
    }
    public class Group {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
    }
    public class GroupOther {
        @SerializedName("name")
        public String name;
    }
    public class LectureHall {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
    }
    public class LectureHallOther {
        @SerializedName("name")
        public String name;
    }
    public class Lecturer {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
    }
    public class LecturerOther {
        @SerializedName("name")
        public String name;
    }
    public class Subject {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
    }
    public class SubjectOther {
        @SerializedName("name")
        public String name;
    }
    public class Time {
        @SerializedName("start")
        public String start;
        @SerializedName("end")
        public String end;
    }
    public class _800935 {
        @SerializedName("date")
        public Date date;
        @SerializedName("classTime")
        public ClassTime classTime;
        @SerializedName("eventType")
        public EventType eventType;
        @SerializedName("groups")
        public List<Group> groups = null;
        @SerializedName("lectureHall")
        public LectureHall lectureHall;
        @SerializedName("lecturer")
        public List<Lecturer> lecturer = null;
        @SerializedName("subject")
        public Subject subject;
        @SerializedName("id")
        public String id;
    }
    public class _9451120 {
        @SerializedName("date")
        public Date date;
        @SerializedName("classTime")
        public ClassTime classTime;
        @SerializedName("eventType")
        public EventType eventType;
        @SerializedName("groups")
        public List<Group> groups = null;
        @SerializedName("lectureHall")
        public LectureHall lectureHall;
        @SerializedName("lecturer")
        public List<Lecturer> lecturer = null;
        @SerializedName("subject")
        public Subject subject;
        @SerializedName("id")
        public String id;
    }
    public class _11301305 {
        @SerializedName("date")
        public Date date;
        @SerializedName("classTime")
        public ClassTime classTime;
        @SerializedName("eventType")
        public EventType eventType;
        @SerializedName("groups")
        public List<Group> groups = null;
        @SerializedName("lectureHall")
        public LectureHall lectureHall;
        @SerializedName("lecturer")
        public List<Lecturer> lecturer = null;
        @SerializedName("subject")
        public Subject subject;
        @SerializedName("id")
        public String id;
    }
    public class _13201455 {
        @SerializedName("date")
        public Date date;
        @SerializedName("classTime")
        public ClassTime classTime;
        @SerializedName("eventType")
        public EventType eventType;
        @SerializedName("groups")
        public List<Group> groups = null;
        @SerializedName("lectureHall")
        public LectureHall lectureHall;
        @SerializedName("lecturer")
        public List<Lecturer> lecturer = null;
        @SerializedName("subject")
        public Subject subject;
        @SerializedName("id")
        public String id;
    }
    public class _15051640 {
        @SerializedName("date")
        public Date date;
        @SerializedName("classTime")
        public ClassTime classTime;
        @SerializedName("eventType")
        public EventType eventType;
        @SerializedName("groups")
        public List<Group> groups = null;
        @SerializedName("lectureHall")
        public LectureHall lectureHall;
        @SerializedName("lecturer")
        public List<Lecturer> lecturer = null;
        @SerializedName("subject")
        public Subject subject;
        @SerializedName("id")
        public String id;
    }
    public class _16501725 {
        @SerializedName("date")
        public Date date;
        @SerializedName("classTime")
        public ClassTime classTime;
        @SerializedName("eventType")
        public EventType eventType;
        @SerializedName("groups")
        public List<Group> groups = null;
        @SerializedName("lectureHall")
        public LectureHall lectureHall;
        @SerializedName("lecturer")
        public List<Lecturer> lecturer = null;
        @SerializedName("subject")
        public Subject subject;
        @SerializedName("id")
        public String id;
    }
}
