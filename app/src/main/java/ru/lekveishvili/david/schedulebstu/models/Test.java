package ru.lekveishvili.david.schedulebstu.models;

import io.realm.RealmObject;

public class Test extends RealmObject{

    private String name;

    public Test() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
