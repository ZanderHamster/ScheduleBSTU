package ru.lekveishvili.david.schedulebstu.di.components;

import javax.inject.Singleton;

import dagger.Component;
import ru.lekveishvili.david.schedulebstu.di.module.AppModule;
import ru.lekveishvili.david.schedulebstu.di.module.UiModule;

@Singleton
@Component(modules = {
        AppModule.class,
        UiModule.class
})
public interface MockAppComponent extends AppComponent {
}