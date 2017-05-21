package ru.lekveishvili.david.schedulebstu.screens.search;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        configureList();
    }

    private void configureList() {
        realm.beginTransaction();
        RealmResults<Event> requestEvent = realm.where(Event.class).findAll();
        if (!requestEvent.isEmpty()) {
            int t = 3;
            String s = String.valueOf(t);
        } else {
            int t = 5;
            String s = String.valueOf(t);
        }
        realm.commitTransaction();
    }

    private void configureToolbar() {
        toolbarTitle.setText(getResources().getString(R.string.bottom_menu_search));
    }
}
