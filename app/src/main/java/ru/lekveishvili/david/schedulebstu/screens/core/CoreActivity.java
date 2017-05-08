package ru.lekveishvili.david.schedulebstu.screens.core;

import android.os.Bundle;

import com.bluelinelabs.conductor.ChangeHandlerFrameLayout;
import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.di.components.AppComponent;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseActivity;
import ru.lekveishvili.david.schedulebstu.screens.base.ParentController;

public class CoreActivity extends BaseActivity {

    @BindView(R.id.core_controller_container)
    ChangeHandlerFrameLayout changeHandlerFrameLayout;

    private Router coreRouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        coreRouter = Conductor.attachRouter(this, changeHandlerFrameLayout, savedInstanceState);
        if (!coreRouter.hasRootController()) {
            coreRouter.setRoot(RouterTransaction.with(new ParentController(ParentController.Tag.HOME)));
        }
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        appComponent
                .plus(new CoreActivityComponent.CoreActivityModule(this))
                .inject(this);
    }
}
