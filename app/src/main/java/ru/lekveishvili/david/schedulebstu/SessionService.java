package ru.lekveishvili.david.schedulebstu;

import javax.inject.Singleton;

@Singleton
public class SessionService {
    private String token = "bf562ef61c8db6b02661e9711ba090124cb1e586954cee522484ec252881d72e55adbdf9a3e1cf4221ff39324caf8df009689d44b3b85e1deb8f023f1ff7fa08";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
