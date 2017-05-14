package ru.lekveishvili.david.schedulebstu.models;


import io.realm.RealmObject;

public class Subject extends RealmObject {
    int id;
    String name;

    public Subject() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int getId() {
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
        private int id;
        private String name;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Subject build() {
            return new Subject(this);
        }
    }
}
