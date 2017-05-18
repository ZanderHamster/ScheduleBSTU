package ru.lekveishvili.david.schedulebstu.models;


import io.realm.RealmObject;

public class Subject extends RealmObject {
    private String id;
    private String name;

    public Subject() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Subject(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }

    public static final class Builder {
        private String id;
        private String name;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Subject build() {
            return new Subject(this);
        }
    }
}
