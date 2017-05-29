package ru.lekveishvili.david.schedulebstu.screens.search;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.models.Group;
import ru.lekveishvili.david.schedulebstu.models.Room;
import ru.lekveishvili.david.schedulebstu.models.Teacher;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;

public class SearchController extends BaseController {
    @BindView(R.id.search_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.search_tabhost)
    TabHost tabHost;


    private Realm realm;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_search, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        realm = Realm.getDefaultInstance();
        configureToolbar();
        configureTabs();
        configureList();
    }

    private void configureTabs() {
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1")
                .setIndicator("Группы")
                .setContent(R.id.search_groups_tab);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2")
                .setIndicator("Преподаватели")
                .setContent(R.id.search_teachers_tab);
        tabHost.addTab(tabSpec);
    }

    private void configureList() {
    }

    private void configureToolbar() {
        toolbarTitle.setText(getResources().getString(R.string.bottom_menu_search));
    }
}
