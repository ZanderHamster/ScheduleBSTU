package ru.lekveishvili.david.schedulebstu.models;

import java.util.Date;
import java.util.List;

public class Day {
    final String name;
    final Date date;
    final List<Event> eventList;

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public Day(final Builder builder) {
        this.name = builder.name;
        this.eventList = builder.eventList;
        this.date = builder.date;
    }

    public static final class Builder {
        private String name;
        private List<Event> eventList;
        private Date date;

        private Builder() {
        }

        public Builder withEventItems(final List<Event> eventItems) {
            this.eventList = eventItems;
            return this;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withDate(final Date date) {
            this.date = date;
            return this;
        }

        public Day build() {
            return new Day(this);
        }

    }
}
