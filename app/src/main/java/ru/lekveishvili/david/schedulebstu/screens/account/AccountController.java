package ru.lekveishvili.david.schedulebstu.screens.account;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import ru.lekveishvili.david.schedulebstu.screens.create_event.CreateNewEventController;
import ru.lekveishvili.david.schedulebstu.service.BottomNavigationService;

public class AccountController extends BaseController {
    @BindView(R.id.account_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.account_out)
    Button btnOut;
    @BindView(R.id.account_create_event)
    Button btnCreateEvent;
    @BindView(R.id.account_user_name)
    TextView tvUserName;
    @BindView(R.id.account_user_group)
    TextView tvUserGroup;
    @BindView(R.id.account_user_type)
    TextView tvUserType;

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
        bottomNavigationService.show();
        realm = Realm.getDefaultInstance();

        RealmResults<Authorization> all = realm.where(Authorization.class).findAll();
        String userType = all.get(0).getTypeUser();
        String userName = all.get(0).getFullName();
        tvUserName.setText(userName);
        if (userType.equals("Преподаватель")) {
            tvUserType.setText("Преподаватель");
            btnCreateEvent.setVisibility(View.VISIBLE);
        }
        if (userType.equals("Студент")) {
            tvUserType.setText("Студент");
        }
        if (all.get(0).getGroups().size() != 0) {
            tvUserGroup.setText(all.get(0).getGroups().get(0).getName());
        }

        configureToolbar();
        btnOut.setOnClickListener(v -> {
            realm.beginTransaction();
            realm.where(Authorization.class)
                    .findAll()
                    .deleteAllFromRealm();
            realm.commitTransaction();
            getRouter().setRoot(RouterTransaction.with(new AuthorizationController(true)));
            getRouter().popController(this);
        });
        btnCreateEvent.setOnClickListener(v -> getRouter().pushController(
                RouterTransaction.with(new CreateNewEventController())));
    }

    private void configureToolbar() {
        toolbarTitle.setText(getResources().getString(R.string.bottom_menu_account));
    }
}
