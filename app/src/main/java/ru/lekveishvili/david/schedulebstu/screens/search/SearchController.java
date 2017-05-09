package ru.lekveishvili.david.schedulebstu.screens.search;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;

public class SearchController extends BaseController {
    @BindView(R.id.search_toolbar_title)
    TextView toolbarTitle;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_search, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        configureToolbar();
    }

    private void configureToolbar() {
        toolbarTitle.setText(getResources().getString(R.string.bottom_menu_search));
    }
}
