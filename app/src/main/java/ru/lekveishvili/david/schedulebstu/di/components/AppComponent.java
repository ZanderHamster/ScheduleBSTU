package ru.lekveishvili.david.schedulebstu.di.components;

import javax.inject.Singleton;

import dagger.Component;
import ru.lekveishvili.david.schedulebstu.di.module.AppModule;
import ru.lekveishvili.david.schedulebstu.di.module.UiModule;
import ru.lekveishvili.david.schedulebstu.screens.account.AccountController;
import ru.lekveishvili.david.schedulebstu.screens.advanced.AdvancedViewController;
import ru.lekveishvili.david.schedulebstu.screens.auth.AuthorizationController;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseActivity;
import ru.lekveishvili.david.schedulebstu.screens.base.ParentController;
import ru.lekveishvili.david.schedulebstu.screens.core.CoreActivityComponent;
import ru.lekveishvili.david.schedulebstu.screens.home.HomeController;
import ru.lekveishvili.david.schedulebstu.screens.home.di.HomeComponent;
import ru.lekveishvili.david.schedulebstu.screens.search_pager.SearchPagerController;

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

    void inject(HomeController controller);

    void inject(AdvancedViewController controller);

    void inject(SearchPagerController controller);

    CoreActivityComponent plus(CoreActivityComponent.CoreActivityModule module);

    HomeComponent plus(HomeComponent.GroupControllerModule module);
}
