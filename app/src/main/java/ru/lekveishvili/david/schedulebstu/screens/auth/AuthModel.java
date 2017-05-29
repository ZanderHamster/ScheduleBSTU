package ru.lekveishvili.david.schedulebstu.screens.auth;

import io.realm.RealmObject;

public class AuthModel extends RealmObject {
    private boolean isAuth;
    private String name;

    public AuthModel() {
    }

    public AuthModel(boolean isAuth, String name) {
        this.isAuth = isAuth;
        this.name = name;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
