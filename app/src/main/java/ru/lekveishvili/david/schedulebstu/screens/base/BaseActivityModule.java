package ru.lekveishvili.david.schedulebstu.screens.base;


import dagger.Module;
import dagger.Provides;
import ru.lekveishvili.david.schedulebstu.di.scopes.ActivityScope;

@Module
public abstract class BaseActivityModule<T extends BaseActivity> {

    protected final T activity;

    public BaseActivityModule(T activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public T provideActivity() {
        return activity;
    }
}
