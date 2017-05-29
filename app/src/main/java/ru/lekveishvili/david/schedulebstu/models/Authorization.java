package ru.lekveishvili.david.schedulebstu.models;


import io.realm.RealmList;
import io.realm.RealmObject;

public class Authorization extends RealmObject {
    private String typeUser;
    private RealmList<Group> groups;
    private String token;
    private String firstName;
    private String secondName;
    private String thirdName;
    private String fullName;


    private Authorization(final Builder builder) {
        this.firstName = builder.firstName;
        this.secondName = builder.secondName;
        this.thirdName = builder.thirdName;
        this.fullName = builder.fullName;
        this.typeUser = builder.typeUser;
        this.groups = builder.groups;
        this.token = builder.token;
    }

    public Authorization() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getTypeUser() {
        return typeUser;
    }

    public RealmList<Group> getGroups() {
        return groups;
    }

    public String getToken() {
        return token;
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

    public static final class Builder {
        private String typeUser;
        private RealmList<Group> groups;
        private String token;
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

        public Builder withToken(String token) {
            this.token = token;
            return this;
        }

        public Builder withGroups(RealmList<Group> groups) {
            this.groups = groups;
            return this;
        }

        public Builder withTypeUser(String typeUser) {
            this.typeUser = typeUser;
            return this;
        }

        public Authorization build() {
            return new Authorization(this);
        }
    }

}
