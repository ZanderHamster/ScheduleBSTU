package ru.lekveishvili.david.schedulebstu.di.components;

import javax.inject.Singleton;

import dagger.Component;
import ru.lekveishvili.david.schedulebstu.di.module.AppModule;
import ru.lekveishvili.david.schedulebstu.di.module.UiModule;
import ru.lekveishvili.david.schedulebstu.screens.account.AccountController;
import ru.lekveishvili.david.schedulebstu.screens.auth.AuthorizationController;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseActivity;
import ru.lekveishvili.david.schedulebstu.screens.base.ParentController;
import ru.lekveishvili.david.schedulebstu.screens.core.CoreActivityComponent;

@Singleton
@Component(modules = {
        AppModule.class,
        UiModule.class
})
public interface AppComponent {
    void inject(BaseActivity activity);

    void inject(ParentController controller);

    void inject(AuthorizationController controller);

    void inject(AccountController controller);

    CoreActivityComponent plus(CoreActivityComponent.CoreActivityModule module);

//    HomeComponent plus(HomeComponent.GroupControllerModule module);
}
