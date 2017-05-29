package ru.lekveishvili.david.schedulebstu.screens.account;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluelinelabs.conductor.RouterTransaction;

import javax.inject.Inject;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.ScheduleBSTUApplication;
import ru.lekveishvili.david.schedulebstu.models.Authorization;
import ru.lekveishvili.david.schedulebstu.screens.auth.AuthorizationController;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;
import ru.lekveishvili.david.schedulebstu.screens.base.ParentController;
import ru.lekveishvili.david.schedulebstu.service.BottomNavigationService;

public class AccountController extends BaseController {
    @BindView(R.id.account_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.account_out)
    TextView out;

    @Inject
    BottomNavigationService bottomNavigationService;

    private Realm realm;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_account, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        ScheduleBSTUApplication.getAppComponent(view.getContext())
                .inject(this);
        realm = Realm.getDefaultInstance();
        configureToolbar();
        out.setOnClickListener(v -> {
            bottomNavigationService.hide();
            realm.beginTransaction();
            realm.where(Authorization.class)
                    .findAll()
                    .deleteAllFromRealm();
            realm.commitTransaction();
            getRouter().setRoot(RouterTransaction.with(new AuthorizationController()));
        });
    }

    private void configureToolbar() {
        toolbarTitle.setText(getResources().getString(R.string.bottom_menu_account));
    }
}
