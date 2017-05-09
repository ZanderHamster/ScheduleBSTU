package ru.lekveishvili.david.schedulebstu.models;


public class Group {
    final int id;
    final String name;

    public static Builder newBuilder() {
        return new Builder();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Group(final Builder builder) {
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

        public Group build() {
            return new Group(this);
        }
    }
}
