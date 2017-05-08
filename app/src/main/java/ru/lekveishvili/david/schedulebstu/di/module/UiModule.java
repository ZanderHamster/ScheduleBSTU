package ru.lekveishvili.david.schedulebstu.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.lekveishvili.david.schedulebstu.service.BottomNavigationService;

@Module
public class UiModule {
    @Singleton
    @Provides
    BottomNavigationService provideBottomNavigationService() {
        return new BottomNavigationService();
    }
}
