package ru.lekveishvili.david.schedulebstu.network.models;

import com.google.gson.annotations.SerializedName;

public class AuthRequest {
    @SerializedName("name")
    public String name;
    @SerializedName("pass")
    public String pass;

    public AuthRequest() {
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public AuthRequest(final Builder builder) {
        this.pass = builder.pass;
        this.name = builder.name;
    }

    public static final class Builder {
        private String name;
        private String pass;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPass(String pass) {
            this.pass = pass;
            return this;
        }

        public AuthRequest build() {
            return new AuthRequest(this);
        }
    }

}
