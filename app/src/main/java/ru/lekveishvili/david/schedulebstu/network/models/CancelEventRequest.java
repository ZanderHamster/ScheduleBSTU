package ru.lekveishvili.david.schedulebstu.network.models;

import com.google.gson.annotations.SerializedName;

public class CancelEventRequest {
    @SerializedName("id")
    public String id;
    @SerializedName("date")
    public String date;


    public CancelEventRequest() {
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public CancelEventRequest(final Builder builder) {
        this.id = builder.id;
        this.date = builder.date;
    }

    public static final class Builder {
        private String id;
        private String date;

        private Builder() {
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withDate(String date) {
            this.date = date;
            return this;
        }

        public CancelEventRequest build() {
            return new CancelEventRequest(this);
        }
    }
}
