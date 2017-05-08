package ru.lekveishvili.david.schedulebstu.screens.search;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;

public class SearchController extends BaseController {
    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_search, container, false);
    }
}
