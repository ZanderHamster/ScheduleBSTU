package ru.lekveishvili.david.schedulebstu.models;


import io.realm.RealmObject;

public class Teacher extends RealmObject {
    private String id;
    private String firstName;
    private String secondName;
    private String thirdName;
    private String fullName;

    public Teacher() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public String getFullName() {
        return fullName;
    }

    public Teacher(final Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.secondName = builder.secondName;
        this.thirdName = builder.thirdName;
        this.fullName = builder.fullName;
    }

    public static final class Builder {
        private String id;
        private String firstName;
        private String secondName;
        private String thirdName;
        private String fullName;

        private Builder() {
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withSecondName(String secondName) {
            this.secondName = secondName;
            return this;
        }

        public Builder withThirdName(String thirdName) {
            this.thirdName = thirdName;
            return this;
        }

        public Builder withFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Teacher build() {
            return new Teacher(this);
        }
    }
}
