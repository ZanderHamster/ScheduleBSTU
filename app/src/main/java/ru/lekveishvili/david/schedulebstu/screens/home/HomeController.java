package ru.lekveishvili.david.schedulebstu.screens.home;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bluelinelabs.conductor.RouterTransaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


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
import ru.lekveishvili.david.schedulebstu.screens.auth.AuthorizationController;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;
import ru.lekveishvili.david.schedulebstu.service.BottomNavigationService;
import ru.lekveishvili.david.schedulebstu.util.Utils;

public class HomeController extends BaseController {
    private final static int START_POSITION_PAGER = 25;
    private int tmpInt = START_POSITION_PAGER;
    private Realm realm;
    private MainApiService apiService = RetrofitClient.getMainApiService();

    private NewPager newPager = new NewPager(new ArrayList<>());
    private Calendar cal = Calendar.getInstance();
    private String strDate = "";

    @BindView(R.id.home_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.home_go_to_auth)
    Button btnGoToAuth;


    @Inject
    BottomNavigationService bottomNavigationService;
    @Inject
    SessionService sessionService;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_home, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        ScheduleBSTUApplication.getAppComponent(view.getContext())
                .inject(this);
        bottomNavigationService.show();
        realm = Realm.getDefaultInstance();
        configureToolbar();
        if (Utils.isOnline(getApplicationContext())) {
            RealmResults<Authorization> all = realm.where(Authorization.class).findAll();

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            String strMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
            if (month < 10) {
                strMonth = 0 + strMonth;
            }
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String strDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            if (day < 10) {
                strDay = 0 + strDay;
            }
            if (all.get(0).getTypeUser().equals("Гость")) {
                btnGoToAuth.setVisibility(View.VISIBLE);
                btnGoToAuth.setOnClickListener(v -> {
                    bottomNavigationService.hide();
                    realm.beginTransaction();
                    realm.where(Authorization.class)
                            .findAll()
                            .deleteAllFromRealm();
                    realm.commitTransaction();
                    getRouter().setRoot(RouterTransaction.with(new AuthorizationController(false)));
                });

            }
            if (all.get(0).getTypeUser().equals("Студент")) {
                strDate = year + "-" + strMonth + "-" + strDay;
                String strGroup = all.get(0).getGroups().get(0).getName();

                new GetEventWeekUseCase(apiService, sessionService.getToken(), strDate, strGroup)
                        .executeGroup()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setModelGroups);

                newPager.setOnItemClickListener(event -> {
                    sessionService.setAdvancedEvent(event);
                    getRouter().pushController(
                            RouterTransaction.with(new AdvancedViewController()));
                });

                RealmResults<Event> groups = realm.where(Event.class)
                        .equalTo("groups.name", strGroup)
                        .between("startEvent", getStartWeek(cal.getTime()), getEndWeek(cal.getTime()))
                        .findAllSorted("startEvent");
                newPager.setEventList(realm.copyFromRealm(groups));
                viewPager.setAdapter(newPager);
                viewPager.setCurrentItem(START_POSITION_PAGER);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (tmpInt < position) {
                            cal.add(Calendar.DATE, 7);
                        } else {
                            cal.add(Calendar.DATE, -7);
                        }
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH) + 1;
                        String strMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
                        if (month < 10) {
                            strMonth = 0 + strMonth;
                        }
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        String strDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                        if (day < 10) {
                            strDay = 0 + strDay;
                        }
                        strDate = year + "-" + strMonth + "-" + strDay;
                        new GetEventWeekUseCase(apiService, sessionService.getToken(), strDate, strGroup)
                                .executeGroup()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(this::setModelGroups);
                        tmpInt = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }

                    private void setModelGroups(RealmList<Event> events) {
                        realm.beginTransaction();
                        RealmResults<Event> all = realm.where(Event.class).findAll();
                        all.deleteAllFromRealm();
                        for (int i = 0; i < events.size(); i++) {
                            realm.copyToRealm(events.get(i));
                        }
                        realm.commitTransaction();
                        RealmResults<Authorization> authorizations = realm.where(Authorization.class).findAll();
                        String group = authorizations.get(0).getGroups().get(0).getName();

                        RealmResults<Event> groups = realm.where(Event.class)
                                .equalTo("groups.name", group)
                                .between("startEvent", getStartWeek(cal.getTime()), getEndWeek(cal.getTime()))
                                .findAllSorted("startEvent");
                        newPager.setEventList(realm.copyFromRealm(groups));
                    }
                });
            }
            if (all.get(0).getTypeUser().equals("Преподаватель")) {
                strDate = year + "-" + strMonth + "-" + strDay;
                String strLecture = all.get(0).getFullName();

                new GetEventWeekUseCase(apiService, sessionService.getToken(), strDate, strLecture)
                        .executeTeacher()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setModelTeacher);

                newPager.setOnItemClickListener(event -> {
                    sessionService.setAdvancedEvent(event);
                    getRouter().pushController(
                            RouterTransaction.with(new AdvancedViewController()));
                });
                RealmResults<Event> groups = realm.where(Event.class)
                        .equalTo("teachers.fullName", strLecture)
                        .between("startEvent", getStartWeek(cal.getTime()), getEndWeek(cal.getTime()))
                        .findAllSorted("startEvent");
                newPager.setEventList(realm.copyFromRealm(groups));
                viewPager.setAdapter(newPager);
                viewPager.setCurrentItem(START_POSITION_PAGER);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (tmpInt < position) {
                            cal.add(Calendar.DATE, 7);
                        } else {
                            cal.add(Calendar.DATE, -7);
                        }
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH) + 1;
                        String strMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
                        if (month < 10) {
                            strMonth = 0 + strMonth;
                        }
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        String strDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                        if (day < 10) {
                            strDay = 0 + strDay;
                        }
                        strDate = year + "-" + strMonth + "-" + strDay;
                        new GetEventWeekUseCase(apiService, sessionService.getToken(), strDate, strLecture)
                                .executeTeacher()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(this::setModelTeacher);
                        tmpInt = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

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
                });
            }

        }
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

    private void setModelGroups(RealmList<Event> events) {
        realm.beginTransaction();
        RealmResults<Event> all = realm.where(Event.class).findAll();
        all.deleteAllFromRealm();
        for (int i = 0; i < events.size(); i++) {
            realm.copyToRealm(events.get(i));
        }
        realm.commitTransaction();
        RealmResults<Authorization> authorizations = realm.where(Authorization.class).findAll();
        String group = authorizations.get(0).getGroups().get(0).getName();

        RealmResults<Event> groups = realm.where(Event.class)
                .equalTo("groups.name", group)
                .between("startEvent", getStartWeek(cal.getTime()), getEndWeek(cal.getTime()))
                .findAllSorted("startEvent");
        newPager.setEventList(realm.copyFromRealm(groups));
    }

    private void configureToolbar() {
        toolbarTitle.setText(getResources().getString(R.string.bottom_menu_home));
    }
}
