package ru.lekveishvili.david.schedulebstu.screens.auth;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.ScheduleBSTUApplication;
import ru.lekveishvili.david.schedulebstu.models.Authorization;
import ru.lekveishvili.david.schedulebstu.network.RetrofitClient;
import ru.lekveishvili.david.schedulebstu.network.models.AuthRequest;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;
import ru.lekveishvili.david.schedulebstu.network.usecase.GetAuthorizationUseCase;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;
import ru.lekveishvili.david.schedulebstu.screens.base.ParentController;
import ru.lekveishvili.david.schedulebstu.screens.base.ParentSubController;
import ru.lekveishvili.david.schedulebstu.screens.home.HomeController;
import ru.lekveishvili.david.schedulebstu.service.BottomNavigationService;

public class AuthorizationController extends BaseController {
    @BindView(R.id.auth_et_login)
    EditText etLogin;
    @BindView(R.id.auth_et_password)
    EditText etPassword;
    @BindView(R.id.auth_enter)
    TextView btnEnter;
    @BindView(R.id.auth_enter_visitor)
    TextView btnEnterVisitor;

    @Inject
    BottomNavigationService bottomNavigationService;

    private Realm realm;
    private GetAuthorizationUseCase getAuthorizationUseCase;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_authorization, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        ScheduleBSTUApplication.getAppComponent(view.getContext())
                .inject(this);
        bottomNavigationService.hide();
        realm = Realm.getDefaultInstance();

        MainApiService apiService = RetrofitClient.getMainApiService();
        getAuthorizationUseCase = new GetAuthorizationUseCase(apiService);

        AutoLogin();

        btnEnter.setOnClickListener(v -> getAuthorizationUseCase.execute(
                AuthRequest.newBuilder()
                        .withName(etLogin.getText().toString())
                        .withPass(etPassword.getText().toString())
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setAuthorization,
                        throwable -> Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show(),
                        () -> startUsing()
                ));

    }

    private void startUsing() {
        bottomNavigationService.show();
        getRouter().pushController(RouterTransaction.with(new HomeController()));
        getRouter().popController(this);
    }


    private void setAuthorization(Authorization authorization) {

        realm.beginTransaction();
        realm.copyToRealm(authorization);
        realm.commitTransaction();
    }

    private void AutoLogin() {
        etLogin.setText("akhremenko-sergey-mikhaylovich");
        etPassword.setText("temp");
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        bottomNavigationService.show();
        super.onDestroyView(view);
    }
}
