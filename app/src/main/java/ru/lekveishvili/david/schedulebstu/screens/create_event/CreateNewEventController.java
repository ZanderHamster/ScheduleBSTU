package ru.lekveishvili.david.schedulebstu.screens.create_event;


import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;

public class CreateNewEventController extends BaseController {
    @BindView(R.id.new_event_toolbar_back_button)
    ImageView btnBack;
    @BindView(R.id.new_event_toolbar_title)
    TextView toolbarTitle;

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        configureToolbar();
    }

    private void configureToolbar() {
        btnBack.setOnClickListener(v -> getRouter().handleBack());
        toolbarTitle.setText(getResources().getString(R.string.create_new_event));
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_create_new_event, container, false);
    }
}
