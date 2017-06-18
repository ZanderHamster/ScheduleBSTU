package ru.lekveishvili.david.schedulebstu.network.models;

import com.google.gson.annotations.SerializedName;

public class EditEventRequest {
    @SerializedName("id")
    public String id;
    @SerializedName("date")
    public String date;
    @SerializedName("newDate")
    public String newDate;
}
