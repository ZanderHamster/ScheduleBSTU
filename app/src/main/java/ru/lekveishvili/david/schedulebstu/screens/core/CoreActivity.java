package ru.lekveishvili.david.schedulebstu.screens.core;

import android.os.Bundle;

import com.bluelinelabs.conductor.ChangeHandlerFrameLayout;
import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.di.components.AppComponent;
import ru.lekveishvili.david.schedulebstu.models.Authorization;
import ru.lekveishvili.david.schedulebstu.screens.auth.AuthModel;
import ru.lekveishvili.david.schedulebstu.screens.auth.AuthorizationController;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseActivity;
import ru.lekveishvili.david.schedulebstu.screens.base.ParentController;
import ru.lekveishvili.david.schedulebstu.screens.home.HomeController;

public class CoreActivity extends BaseActivity {
    @BindView(R.id.core_controller_container)
    ChangeHandlerFrameLayout changeHandlerFrameLayout;

    private Router coreRouter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        coreRouter = Conductor.attachRouter(this, changeHandlerFrameLayout, savedInstanceState);
        if (!coreRouter.hasRootController()) {
            navigateToStart();
        }
    }

    private void navigateToStart() {
        RealmResults<Authorization> isAuth = realm.where(Authorization.class).findAll();
        if (isAuth.size() != 0) {
            coreRouter.setRoot(RouterTransaction.with(new ParentController(ParentController.Tag.HOME)));
        } else {
            coreRouter.setRoot(RouterTransaction.with(new AuthorizationController(false)));
        }
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        appComponent
                .plus(new CoreActivityComponent.CoreActivityModule(this))
                .inject(this);
    }
}
