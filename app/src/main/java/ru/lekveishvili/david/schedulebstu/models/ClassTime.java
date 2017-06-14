package ru.lekveishvili.david.schedulebstu.models;


import io.realm.RealmObject;

public class ClassTime extends RealmObject {
    private String id;
    private String start;
    private String end;

    public ClassTime() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public ClassTime(final Builder builder) {
        this.id = builder.id;
        this.start = builder.start;
        this.end = builder.end;
    }

    public static final class Builder {
        private String id;
        private String start;
        private String end;

        private Builder() {
        }


        public Builder withId(String id) {
            this.id = id;
            return this;
        }
        public Builder withStart(String start) {
            this.start = start;
            return this;
        }
        public Builder withEnd(String end) {
            this.end = end;
            return this;
        }

        public ClassTime build() {
            return new ClassTime(this);
        }
    }
}
