package ru.lekveishvili.david.schedulebstu.screens.advanced;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.ScheduleBSTUApplication;
import ru.lekveishvili.david.schedulebstu.SessionService;
import ru.lekveishvili.david.schedulebstu.models.Authorization;
import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;

public class AdvancedViewController extends BaseController {
    private Event advancedEvent;
    private Realm realm;
    @BindView(R.id.advanced_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.advanced_toolbar_back_button)
    ImageView btnBack;

    @BindView(R.id.advanced_subject)
    TextView tvSubject;
    @BindView(R.id.advanced_event_type)
    TextView tvEventType;
    @BindView(R.id.advanced_time)
    TextView tvTime;
    @BindView(R.id.advanced_room)
    TextView tvRoom;
    @BindView(R.id.advanced_teachers)
    TextView tvTeachers;
    @BindView(R.id.advanced_groups)
    TextView tvGroups;

    @BindView(R.id.advanced_delete)
    Button btnDelete;
    @BindView(R.id.advanced_edit)
    Button btnEdit;


    @Inject
    SessionService sessionService;

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        ScheduleBSTUApplication.getAppComponent(view.getContext())
                .inject(this);
        realm = Realm.getDefaultInstance();
        configureToolbar();
        configureInfo();
    }

    private void configureInfo() {
        advancedEvent = sessionService.getAdvancedEvent();
        tvSubject.setText(advancedEvent.getSubject().getName());
        tvEventType.setText(advancedEvent.getEventType().getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", new Locale("ru", "RU"));
        tvTime.setText(
                dateFormat.format(advancedEvent.getStartEvent()) + "-" +
                        dateFormat.format(advancedEvent.getEndEvent())
        );
        tvRoom.setText(advancedEvent.getRoom().getName());

        String teachers = "";
        for (int i = 0; i < advancedEvent.getTeachers().size(); i++) {
            teachers += advancedEvent.getTeachers().get(i).getFullName() + ", ";
        }
        teachers = teachers.substring(0, teachers.length() - 2);
        tvTeachers.setText(teachers);

        String groups = "";
        for (int i = 0; i < advancedEvent.getGroups().size(); i++) {
            groups += advancedEvent.getGroups().get(i).getName() + ", ";
        }
        groups = groups.substring(0, groups.length() - 2);
        tvGroups.setText(groups);

        RealmResults<Authorization> all = realm.where(Authorization.class).findAll();
        String fullName = all.get(0).getFullName();
        boolean isOwner = false;
        for (int i = 0; i < advancedEvent.getTeachers().size(); i++) {
            if (fullName.equals(advancedEvent.getTeachers().get(i).getFullName())) {
                isOwner = true;
            }
        }
        if (isOwner) {
            btnDelete.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(v -> {
                //TODO удаление
            });
            btnEdit.setOnClickListener(v -> {
                //TODO редактирование
            });
        }

    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_advanced, container, false);
    }

    private void configureToolbar() {
        btnBack.setOnClickListener(v -> getRouter().handleBack());
        toolbarTitle.setText(getResources().getString(R.string.advanced));
    }
}
