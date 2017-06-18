package ru.lekveishvili.david.schedulebstu.screens.advanced;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.lekveishvili.david.schedulebstu.NotificationPublisher;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.ScheduleBSTUApplication;
import ru.lekveishvili.david.schedulebstu.SessionService;
import ru.lekveishvili.david.schedulebstu.models.Authorization;
import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.network.RetrofitClient;
import ru.lekveishvili.david.schedulebstu.network.models.CancelEventRequest;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;
import ru.lekveishvili.david.schedulebstu.network.usecase.GetDeleteEventDataUseCase;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;

public class AdvancedViewController extends BaseController {
    private Event advancedEvent;
    private Realm realm;

    private MainApiService apiService = RetrofitClient.getMainApiService();
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
    @BindView(R.id.advanced_date)
    TextView tvDate;
    @BindView(R.id.advanced_notif)
    ImageView btnNotif;

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

        btnNotif.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            Calendar current = Calendar.getInstance();
            cal.setTime(advancedEvent.getStartEvent());
            if (cal.compareTo(current) <= 0)
                Toast.makeText(getApplicationContext(), "Событие уже прошло", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(getApplicationContext(), "Подписка оформленна", Toast.LENGTH_SHORT).show();
//                scheduleNotification(getNotification(), cal);
            }
        });
    }

    private void scheduleNotification(Notification notification, Calendar targetCal) {
        Intent notificationIntent = new Intent(getApplicationContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, notificationIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= 19)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        else alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }

    private void configureInfo() {
        advancedEvent = sessionService.getAdvancedEvent();
        tvSubject.setText(advancedEvent.getSubject().getName());
        tvEventType.setText(advancedEvent.getEventType().getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", new Locale("ru", "RU"));
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy", new Locale("ru", "RU"));
        tvTime.setText(
                dateFormat.format(advancedEvent.getStartEvent()) + "-" +
                        dateFormat.format(advancedEvent.getEndEvent())
        );

        tvDate.setText(dateFormat2.format(advancedEvent.getStartEvent()));


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

                String token = "";
                RealmResults<Authorization> accounts = realm.where(Authorization.class).findAll();
                if (accounts.size() != 0) {
                    token = accounts.get(0).getToken();
                }
                Date startEvent = advancedEvent.getStartEvent();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", new Locale("ru", "RU"));

                CancelEventRequest cancelEventRequest = new CancelEventRequest();
                cancelEventRequest.id = advancedEvent.getId();
                cancelEventRequest.date = df.format(startEvent);

                realm.beginTransaction();
                realm.where(Event.class)
                        .equalTo("id", advancedEvent.getId())
                        .equalTo("startEvent", advancedEvent.getStartEvent())
                        .equalTo("endEvent", advancedEvent.getEndEvent())
                        .findAll()
                        .deleteFirstFromRealm();
                realm.commitTransaction();

                new GetDeleteEventDataUseCase(apiService, token)
                        .execute(cancelEventRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::deleteEvent);

                getRouter().handleBack();
            });
            btnEdit.setOnClickListener(v -> {
                //TODO редактирование
            });
        }

    }

    private void deleteEvent(boolean deletion) {

    }

    private Notification getNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_date_range_black_24dp);
        Intent intent = new Intent(getApplicationContext(), NotificationPublisher.class);
        intent.putExtra("title", "test");
        intent.putExtra("text", "test");
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_date_range_black_24dp)
                .setLargeIcon(bitmap)
                .setContentTitle("title")
                .setContentText("content")
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                .setDefaults(Notification.DEFAULT_SOUND);

        return builder.build();
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
