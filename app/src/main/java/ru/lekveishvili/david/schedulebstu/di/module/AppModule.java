package ru.lekveishvili.david.schedulebstu.di.module;

import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.lekveishvili.david.schedulebstu.ScheduleBSTUApplication;

@Module
public class AppModule {
    private final ScheduleBSTUApplication application;

    public AppModule(ScheduleBSTUApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    Resources provideResources() {
        return application.getResources();
    }
}
