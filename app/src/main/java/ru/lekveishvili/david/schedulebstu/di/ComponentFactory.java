package ru.lekveishvili.david.schedulebstu.di;


import ru.lekveishvili.david.schedulebstu.ScheduleBSTUApplication;
import ru.lekveishvili.david.schedulebstu.di.components.AppComponent;
import ru.lekveishvili.david.schedulebstu.di.components.DaggerMockAppComponent;
import ru.lekveishvili.david.schedulebstu.di.module.AppModule;

public class ComponentFactory {
    public static AppComponent create(ScheduleBSTUApplication context) {
        return DaggerMockAppComponent.builder()
                .appModule(new AppModule(context))
                .build();
    }
}
