package ru.lekveishvili.david.schedulebstu.screens.core;

import dagger.Module;
import dagger.Subcomponent;
import ru.lekveishvili.david.schedulebstu.di.scopes.ActivityScope;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseActivityModule;

@ActivityScope
@Subcomponent(modules = CoreActivityComponent.CoreActivityModule.class)
public interface CoreActivityComponent {
    void inject(CoreActivity activity);

    @Module
    class CoreActivityModule extends BaseActivityModule<CoreActivity> {

        public CoreActivityModule(CoreActivity activity) {
            super(activity);
        }
    }
}
