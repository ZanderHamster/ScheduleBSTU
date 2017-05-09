package ru.lekveishvili.david.schedulebstu.screens.account;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;

public class AccountController extends BaseController {
    @BindView(R.id.account_toolbar_title)
    TextView toolbarTitle;
    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_account, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        configureToolbar();
    }

    private void configureToolbar() {
        toolbarTitle.setText(getResources().getString(R.string.bottom_menu_account));
    }
}
