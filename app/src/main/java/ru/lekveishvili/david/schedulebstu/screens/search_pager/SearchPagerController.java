package ru.lekveishvili.david.schedulebstu.screens.search_pager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluelinelabs.conductor.RouterTransaction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.ScheduleBSTUApplication;
import ru.lekveishvili.david.schedulebstu.SessionService;
import ru.lekveishvili.david.schedulebstu.models.Authorization;
import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.network.RetrofitClient;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;
import ru.lekveishvili.david.schedulebstu.network.usecase.GetEventWeekUseCase;
import ru.lekveishvili.david.schedulebstu.screens.advanced.AdvancedViewController;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;
import ru.lekveishvili.david.schedulebstu.screens.home.NewPager;
import ru.lekveishvili.david.schedulebstu.util.BundleBuilder;

public class SearchPagerController extends BaseController {
    private static final String KEY_NAME = "NAME";
    private static final String KEY_DATE = "DATE";
    private Realm realm = Realm.getDefaultInstance();
    private MainApiService apiService = RetrofitClient.getMainApiService();
    private String name;
    private String date;
    private NewPager newPager = new NewPager(new ArrayList<>());
    private Calendar cal = Calendar.getInstance();


    @BindView(R.id.search_pager_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.search_pager_toolbar_back_button)
    ImageView btnBack;

    @BindView(R.id.search_view_pager)
    ViewPager viewPager;


    @Inject
    SessionService sessionService;

    public SearchPagerController(String name, String date) {
        this(new BundleBuilder(new Bundle())
                .putString(KEY_NAME, name)
                .putString(KEY_DATE, date)
                .build()
        );
    }

    @SuppressWarnings("WeakerAccess")
    public SearchPagerController(Bundle args) {
        super(args);
        name = getArgs().getString(KEY_NAME, "");
        date = getArgs().getString(KEY_DATE, "");

    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        ScheduleBSTUApplication.getAppComponent(view.getContext())
                .inject(this);

        configureToolbar();

        configureTeacher();
    }

    private void configureTeacher() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", new Locale("ru", "RU"));
        Date parse = new Date();
        try {
            parse = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(parse);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);

        new GetEventWeekUseCase(apiService, sessionService.getToken(), date, name)
                .executeTeacher()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setModelTeacher,
                        throwable -> {},
                        ()-> newPager.notifyDataSetChanged());

        newPager.setOnItemClickListener(event -> {
            sessionService.setAdvancedEvent(event);
            getRouter().pushController(
                    RouterTransaction.with(new AdvancedViewController()));
        });
        viewPager.setAdapter(newPager);

    }

    private void setModelTeacher(RealmList<Event> events) {
        realm.beginTransaction();
        RealmResults<Event> all = realm.where(Event.class).findAll();
        all.deleteAllFromRealm();
        for (int i = 0; i < events.size(); i++) {
            realm.copyToRealm(events.get(i));
        }
        realm.commitTransaction();
        RealmResults<Authorization> authorizations = realm.where(Authorization.class).findAll();
        String fullName = authorizations.get(0).getFullName();

        RealmResults<Event> groups = realm.where(Event.class)
                .equalTo("teachers.fullName", fullName)
                .between("startEvent", getStartWeek(cal.getTime()), getEndWeek(cal.getTime()))
                .findAllSorted("startEvent");
        newPager.setEventList(realm.copyFromRealm(groups));
    }

    private Date getStartWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DATE, -1);
        }
        return cal.getTime();
    }

    private Date getEndWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DATE, 1);
        }
        return cal.getTime();
    }

    private void configureToolbar() {
        btnBack.setOnClickListener(v -> getRouter().handleBack());
        toolbarTitle.setText("ПРОВЕРКА");
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_search_pager, container, false);
    }
}
