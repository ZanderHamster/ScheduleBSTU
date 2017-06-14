package ru.lekveishvili.david.schedulebstu.models;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Event extends RealmObject {
    private String id;
    private Date startEvent;
    private Date endEvent;
    private EventType eventType;
    private Room room;
    private RealmList<Teacher> teachers;
    private Subject subject;
    private RealmList<Group> groups;
    private String timeId;

    public Event() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Event oldModel) {
        return new Builder(oldModel);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStartEvent(Date startEvent) {
        this.startEvent = startEvent;
    }

    public void setEndEvent(Date endEvent) {
        this.endEvent = endEvent;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setTeachers(RealmList<Teacher> teachers) {
        this.teachers = teachers;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setGroups(RealmList<Group> group) {
        this.groups = group;
    }

    public Date getStartEvent() {
        return startEvent;
    }

    public Date getEndEvent() {
        return endEvent;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Room getRoom() {
        return room;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public Subject getSubject() {
        return subject;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public String getTimeId() {
        return timeId;
    }

    public Event(final Builder builder) {
        this.id = builder.id;
        this.startEvent = builder.startEvent;
        this.endEvent = builder.endEvent;
        this.eventType = builder.eventType;
        this.room = builder.room;
        this.teachers = builder.teachers;
        this.subject = builder.subject;
        this.groups = builder.groups;
        this.timeId = builder.timeId;
    }

    public static final class Builder {
        private String id;
        private Date startEvent;
        private Date endEvent;
        private EventType eventType;
        private Room room;
        private RealmList<Teacher> teachers;
        private Subject subject;
        private RealmList<Group> groups;
        private String timeId;

        private Builder() {
        }

        private Builder(final Event copy) {
            this.id = copy.id;
            this.startEvent = copy.startEvent;
            this.endEvent = copy.endEvent;
            this.eventType = copy.eventType;
            this.room = copy.room;
            this.teachers = copy.teachers;
            this.subject = copy.subject;
            this.groups = copy.groups;
            this.timeId = copy.timeId;
        }

        public Builder withId(final String id) {
            this.id = id;
            return this;
        }
        public Builder withTimeId(final String timeId) {
            this.timeId = timeId;
            return this;
        }

        public Builder withEventType(final EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder withStartEvent(final Date startEvent) {
            this.startEvent = startEvent;
            return this;
        }

        public Builder withEndEvent(final Date endEvent) {
            this.endEvent = endEvent;
            return this;
        }

        public Builder withRoom(final Room room) {
            this.room = room;
            return this;
        }

        public Builder withTeachers(final RealmList<Teacher> teachers) {
            this.teachers = teachers;
            return this;
        }

        public Builder withSubject(final Subject subject) {
            this.subject = subject;
            return this;
        }

        public Builder withGroups(final RealmList<Group> groups) {
            this.groups = groups;
            return this;
        }

        public Event build() {
            return new Event(this);
        }

    }
}
