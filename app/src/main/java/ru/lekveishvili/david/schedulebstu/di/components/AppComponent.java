package ru.lekveishvili.david.schedulebstu.di.components;

import ru.lekveishvili.david.schedulebstu.screens.base.BaseActivity;
import ru.lekveishvili.david.schedulebstu.screens.base.ParentController;
import ru.lekveishvili.david.schedulebstu.screens.core.CoreActivityComponent;

public interface AppComponent {
    void inject(BaseActivity activity);

    void inject(ParentController controller);

    CoreActivityComponent plus(CoreActivityComponent.CoreActivityModule module);
}
