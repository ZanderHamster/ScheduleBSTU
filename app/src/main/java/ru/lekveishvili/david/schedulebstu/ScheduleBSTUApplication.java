package ru.lekveishvili.david.schedulebstu;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.lekveishvili.david.schedulebstu.di.ComponentFactory;
import ru.lekveishvili.david.schedulebstu.di.components.AppComponent;

public class ScheduleBSTUApplication extends Application {
    private AppComponent appComponent;

    public static AppComponent getAppComponent(Context context) {
        return get(context).appComponent;
    }

    public static ScheduleBSTUApplication get(Context context) {
        return (ScheduleBSTUApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .build();
        Realm.setDefaultConfiguration(config);
        setupAppComponent();
    }

    private void setupAppComponent() {
        appComponent = ComponentFactory.create(this);
    }
}
