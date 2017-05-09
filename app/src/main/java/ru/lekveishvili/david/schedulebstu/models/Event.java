package ru.lekveishvili.david.schedulebstu.models;

import java.util.Date;

public class Event {
    final Date startEvent;
    final Date endEvent;
    final EventType eventType;
    final Room room;
    final Teacher teacher;
    final Subject subject;
    final Group group;

    public static Builder newBuilder() {
        return new Builder();
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

    public Teacher getTeacher() {
        return teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public Group getGroup() {
        return group;
    }

    public Event(final Builder builder) {
        this.startEvent = builder.startEvent;
        this.endEvent = builder.endEvent;
        this.eventType = builder.eventType;
        this.room = builder.room;
        this.teacher = builder.teacher;
        this.subject = builder.subject;
        this.group = builder.group;
    }

    public static final class Builder {
        private Date startEvent;
        private Date endEvent;
        private EventType eventType;
        private Room room;
        private Teacher teacher;
        private Subject subject;
        private Group group;

        private Builder() {
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

        public Builder withTeacher(final Teacher teacher) {
            this.teacher = teacher;
            return this;
        }

        public Builder withSubject(final Subject subject) {
            this.subject = subject;
            return this;
        }

        public Builder withGroup(final Group group) {
            this.group = group;
            return this;
        }

        public Event build() {
            return new Event(this);
        }

    }
}
