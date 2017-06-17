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
    private Realm realm;
    private MainApiService apiService = RetrofitClient.getMainApiService();
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
            Calendar instance = Calendar.getInstance();
            int year = instance.get(Calendar.YEAR);
            int month = instance.get(Calendar.MONTH) + 1;
            String strMonth = String.valueOf(instance.get(Calendar.MONTH) + 1);
            if (month < 10) {
                strMonth = 0 + strMonth;
            }
            int day = instance.get(Calendar.DAY_OF_MONTH);
            String strDay = String.valueOf(instance.get(Calendar.DAY_OF_MONTH));
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
                String strDate = year + "-" + strMonth + "-" + strDay;
                String strGroup = all.get(0).getGroups().get(0).getName();


                GetEventWeekUseCase getEventWeekUseCase = new GetEventWeekUseCase(
                        apiService,
                        sessionService.getToken(),
                        strDate, strGroup);
                getEventWeekUseCase.executeBasicGroup()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setModel);
            }
            if (all.get(0).getTypeUser().equals("Преподаватель")) {
                String strDate = year + "-" + strMonth + "-" + strDay;
                String strLecture = all.get(0).getFullName();

                GetEventWeekUseCase getEventWeekUseCase = new GetEventWeekUseCase(
                        apiService,
                        sessionService.getToken(),
                        strDate, strLecture);
                getEventWeekUseCase.executeBasicTeacher()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setModelTeacher);
            }

        }
    }

    private void setModel(RealmList<Event> eventsWeek) {

        List<List<Event>> weeks = new ArrayList<>();
        if (eventsWeek.size() != 0) {
            Date startPeriod = eventsWeek.get(0).getStartEvent();
            Date endPeriod = eventsWeek.get(0).getEndEvent();
            for (int i = 0; i < eventsWeek.size(); i++) {
                if (eventsWeek.get(i).getStartEvent().before(startPeriod)) {
                    startPeriod = eventsWeek.get(i).getStartEvent();
                }
                if (eventsWeek.get(i).getEndEvent().after(endPeriod)) {
                    endPeriod = eventsWeek.get(i).getEndEvent();
                }
            }

            realm.beginTransaction();
            RealmResults<Event> all = realm.where(Event.class).findAll();
            all.deleteAllFromRealm();
            for (int i = 0; i < eventsWeek.size(); i++) {
                realm.copyToRealm(eventsWeek.get(i));
            }
            realm.commitTransaction();
            if (startPeriod != null && endPeriod != null) {
                Calendar startWeek = Calendar.getInstance();
                Calendar endWeek = Calendar.getInstance();
                startWeek.setTime(startPeriod);
                startWeek.set(Calendar.HOUR_OF_DAY, 1);
                endWeek.setTime(startPeriod);

                while (endWeek.getTime().getTime() < endPeriod.getTime()) {
                    while (endWeek.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                        endWeek.add(Calendar.DATE, 1);
                    }
                    startWeek.set(Calendar.HOUR_OF_DAY, 1);
                    Date dateStartWeek = startWeek.getTime();
                    Date dateEndWeek = endWeek.getTime();
                    RealmResults<Event> groups = realm.where(Event.class)
                            .equalTo("groups.name", "13-ИВТ1")
                            .between("startEvent", dateStartWeek, dateEndWeek)
                            .findAllSorted("startEvent");
                    List<Event> eventList = realm.copyFromRealm(groups);
                    weeks.add(eventList);
                    endWeek.add(Calendar.DATE, 1);
                    Date time = endWeek.getTime();
                    startWeek.setTime(time);
                }

                Pager pager = new Pager(weeks);
                pager.setOnItemClickListener(event -> {
                    sessionService.setAdvancedEvent(event);
                    getRouter().pushController(
                            RouterTransaction.with(new AdvancedViewController()));
                });
                viewPager.setAdapter(pager);
                pager.setCurrentWeek(sessionService.getCurrentSelectedWeek());
                viewPager.setCurrentItem(sessionService.getCurrentSelectedWeek());
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        viewPager.setCurrentItem(position);
                        pager.setCurrentWeek(position);
                        sessionService.setCurrentSelectedWeek(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        }
    }

    private Date getStartWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DATE, -1);
        }
        return cal.getTime();
    }

    private Date getEndWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DATE, 1);
        }
        return cal.getTime();
    }

    private void setModelTeacher(RealmList<Event> events) {
        List<Event> eventList = new ArrayList<>();

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
                .between("startEvent", getStartWeek(), getEndWeek())
                .findAllSorted("startEvent");
        eventList = realm.copyFromRealm(groups);

        NewPager newPager = new NewPager(eventList);
        viewPager.setAdapter(newPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        int t = 4;

    }

    private void configureToolbar() {
        toolbarTitle.setText(getResources().getString(R.string.bottom_menu_home));
    }
}
