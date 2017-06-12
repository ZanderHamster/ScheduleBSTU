package ru.lekveishvili.david.schedulebstu.screens.base;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.ScheduleBSTUApplication;
import ru.lekveishvili.david.schedulebstu.SessionService;
import ru.lekveishvili.david.schedulebstu.models.Event;
import ru.lekveishvili.david.schedulebstu.models.EventType;
import ru.lekveishvili.david.schedulebstu.models.Group;
import ru.lekveishvili.david.schedulebstu.models.Room;
import ru.lekveishvili.david.schedulebstu.models.Subject;
import ru.lekveishvili.david.schedulebstu.models.Teacher;
import ru.lekveishvili.david.schedulebstu.network.RetrofitClient;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;
import ru.lekveishvili.david.schedulebstu.network.usecase.GetEventTypeUseCase;
import ru.lekveishvili.david.schedulebstu.network.usecase.GetEventWeekUseCase;
import ru.lekveishvili.david.schedulebstu.network.usecase.GetGroupUseCase;
import ru.lekveishvili.david.schedulebstu.network.usecase.GetRoomUseCase;
import ru.lekveishvili.david.schedulebstu.network.usecase.GetSubjectUseCase;
import ru.lekveishvili.david.schedulebstu.network.usecase.GetTeacherUseCase;
import ru.lekveishvili.david.schedulebstu.screens.account.AccountController;
import ru.lekveishvili.david.schedulebstu.screens.home.HomeController;
import ru.lekveishvili.david.schedulebstu.screens.search.SearchController;
import ru.lekveishvili.david.schedulebstu.service.BottomNavigationService;
import ru.lekveishvili.david.schedulebstu.util.BundleBuilder;
import ru.lekveishvili.david.schedulebstu.util.Utils;

/**
 * Главный контроллер, контролирующий экраны по нажатиям на нижнюю плашку
 */
public class ParentController extends BaseController {
    private static final String KEY_TAG = "tag";

    @BindView(R.id.main_bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.parent_root)
    ViewGroup parentSubControllersContainer;
    @Inject
    BottomNavigationService bottomNavigationService;
    @Inject
    SessionService sessionService;


    private Realm realm;
    private Tag tag;
    private Router bottomNavigationRouter;
    private Disposable bottomNavigationSubscription;

    public ParentController(Tag tag) {
        this(new BundleBuilder(new Bundle())
                .putString(KEY_TAG, tag.toString())
                .build()
        );
    }

    @SuppressWarnings({"WeakerAccess"}) // нужен для Conductor
    public ParentController(Bundle args) {
        super(args);
        this.tag = ParentController.Tag.valueOf(getArgs().getString(KEY_TAG, Tag.HOME.toString()));
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_parent, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        realm = Realm.getDefaultInstance();
        Activity activity = getActivity();
        if (activity == null) return;
        ScheduleBSTUApplication.getAppComponent(activity)
                .inject(this);

        bottomNavigationRouter = getChildRouter(parentSubControllersContainer);
        setRouterRootIfNeeded();
        configureBottomNavMenu();
        subscribeToBottomNavigationState();
        fetchData();
    }


    private void subscribeToBottomNavigationState() {
        bottomNavigationSubscription = bottomNavigationService.getBottomNavigationVisibilityObservable()
                .debounce(100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(visible -> {
                    if (visible) {
                        showBottomNavigation();
                    } else {
                        hideBottomNavigation();
                    }
                });
    }

    private void hideBottomNavigation() {
        if (bottomNavigationView.getVisibility() == View.GONE) return;
        Resources resources = getResources();
        if (resources == null) return;

        bottomNavigationView.setVisibility(View.GONE);
        ViewGroup.LayoutParams layoutParams = parentSubControllersContainer.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            resources.getDimension(R.dimen.bottom_navigation_view_height);
            marginLayoutParams.setMargins(
                    marginLayoutParams.leftMargin,
                    marginLayoutParams.topMargin,
                    marginLayoutParams.rightMargin,
                    0
            );
        }
    }

    private void showBottomNavigation() {
        if (bottomNavigationView.getVisibility() == View.VISIBLE) return;
        Resources resources = getResources();
        if (resources == null) return;
        bottomNavigationView.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams layoutParams = parentSubControllersContainer.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;

            marginLayoutParams.setMargins(
                    marginLayoutParams.leftMargin,
                    marginLayoutParams.topMargin,
                    marginLayoutParams.rightMargin,
                    (int) resources.getDimension(R.dimen.bottom_navigation_view_height)
            );
        }
    }

    private void configureBottomNavMenu() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    goToSubControllerByTag(Tag.HOME);
                    break;
                case R.id.menu_search:
                    goToSubControllerByTag(Tag.SEARCH);
                    break;
                case R.id.menu_account:
                    goToSubControllerByTag(Tag.ACCOUNT);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    private void goToSubControllerByTag(Tag tag) {
        List<RouterTransaction> backstack = bottomNavigationRouter.getBackstack();
        RouterTransaction transaction;
        Controller controller;
        boolean controllerAlreadyInBackstack = false;
        ListIterator<RouterTransaction> iterator = backstack.listIterator();
        while (iterator.hasNext()) {
            transaction = iterator.next();
            if (tag.toString().equals(transaction.tag())) {
                controllerAlreadyInBackstack = true;
                if (iterator.hasNext()) {
                    iterator.remove();
                    backstack.add(transaction);
                    bottomNavigationRouter.setBackstack(backstack, null);
                } else {
                    controller = transaction.controller();
                    Router childRouter = ((ParentSubController) controller).getChildRouter();
                    childRouter.popToRoot();
                }
                break;
            }
        }

        if (!controllerAlreadyInBackstack) {
            controller = new ParentSubController(tag);
            transaction = RouterTransaction.with(controller).tag(tag.toString());
            bottomNavigationRouter.pushController(transaction);
        }

    }

    private void setRouterRootIfNeeded() {
        if (bottomNavigationRouter == null) {
            bottomNavigationRouter = getChildRouter(parentSubControllersContainer, tag.toString());
        }

        if (bottomNavigationRouter.hasRootController()) return;

        Controller controller = new ParentSubController(tag);
        bottomNavigationRouter.setRoot(RouterTransaction.with(controller));
    }

    @Override
    protected void onDestroyView(@NonNull final View view) {
        super.onDestroyView(view);
        realm.close();
        if (bottomNavigationSubscription != null && !bottomNavigationSubscription.isDisposed()) {
            bottomNavigationSubscription.dispose();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(KEY_TAG, tag);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tag = (Tag) savedInstanceState.getSerializable(KEY_TAG);
    }

    public enum Tag {
        HOME,
        SEARCH,
        ACCOUNT
    }


    private void fetchData() {
        if (Utils.isOnline(getApplicationContext())) {
            MainApiService apiService = RetrofitClient.getMainApiService();

            GetGroupUseCase getGroupUseCase = new GetGroupUseCase(apiService, sessionService.getToken());
            GetRoomUseCase getRoomUseCase = new GetRoomUseCase(apiService, sessionService.getToken());
            GetSubjectUseCase getSubjectUseCase = new GetSubjectUseCase(apiService, sessionService.getToken());
            GetTeacherUseCase getTeacherUseCase = new GetTeacherUseCase(apiService, sessionService.getToken());
            GetEventTypeUseCase getEventTypeUseCase = new GetEventTypeUseCase(apiService, sessionService.getToken());
//            GetEventWeekUseCase getEventWeekUseCase = new GetEventWeekUseCase(apiService, sessionService.getToken());

            getGroupUseCase.execute()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            this::setGroups
                    );
            getRoomUseCase.execute()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            this::setRooms
                    );
            getSubjectUseCase.execute()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            this::setSubjects
                    );
            getTeacherUseCase.execute()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            this::setTeachers
                    );
            getEventTypeUseCase.execute()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            this::setEventTypes
                    );
//            getEventWeekUseCase.executeBasicGroup()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            this::setEventsWeek
//                    );
        }

    }

    ///----------------///
    private void setEventsWeek(RealmList<Event> eventsWeek) {
        realm.beginTransaction();
        RealmResults<Event> all = realm.where(Event.class).findAll();
        all.deleteAllFromRealm();
        for (int i = 0; i < eventsWeek.size(); i++) {
            realm.copyToRealm(eventsWeek.get(i));
        }
        realm.commitTransaction();
        realm.close();
    }


    ///----------------///
    private void setEventTypes(List<EventType> eventTypes) {
        realm.beginTransaction();
        RealmResults<EventType> requestEventTypes = realm.where(EventType.class).findAll();
        List<EventType> tmpEventTypeList = new ArrayList<>();
        for (int i = 0; i < requestEventTypes.size(); i++) {
            tmpEventTypeList.add(requestEventTypes.get(i));
        }
        for (int i = 0; i < eventTypes.size(); i++) {
            if (containsEventType(tmpEventTypeList, eventTypes.get(i))) {
                tmpEventTypeList = removeItemFromListEventType(tmpEventTypeList, eventTypes.get(i));
            } else {
                realm.copyToRealm(eventTypes.get(i));
            }
        }
        for (int i = 0; i < tmpEventTypeList.size(); i++) {
            tmpEventTypeList.get(i).deleteFromRealm();
        }
        realm.commitTransaction();
    }

    private boolean containsEventType(List<EventType> list, EventType item) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(item.getId())) {
                return true;
            }
        }
        return false;
    }

    private List<EventType> removeItemFromListEventType(List<EventType> list, EventType item) {
        List<EventType> tmpList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getName().equals(item.getName())) {
                tmpList.add(list.get(i));
            }
        }
        return tmpList;
    }

    ///----------------///
    private void setTeachers(List<Teacher> teachers) {
        realm.beginTransaction();
        RealmResults<Teacher> requestTeacher = realm.where(Teacher.class).findAll();
//        requestTeacher.deleteAllFromRealm();
//        for (int i = 0; i < teachers.size(); i++) {
//            realm.copyToRealm(teachers.get(i));
//        }
        List<Teacher> tmpTeacherList = new ArrayList<>();
        for (int i = 0; i < requestTeacher.size(); i++) {
            tmpTeacherList.add(requestTeacher.get(i));
        }
        for (int i = 0; i < teachers.size(); i++) {
            if (containsTeacher(tmpTeacherList, teachers.get(i))) {
                tmpTeacherList = removeItemFromListTeacher(tmpTeacherList, teachers.get(i));
            } else {
                realm.copyToRealm(teachers.get(i));
            }
        }
        for (int i = 0; i < tmpTeacherList.size(); i++) {
            tmpTeacherList.get(i).deleteFromRealm();
        }
        realm.commitTransaction();


    }

    private boolean containsTeacher(List<Teacher> list, Teacher item) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(item.getId())) {
                return true;
            }
        }
        return false;
    }

    private List<Teacher> removeItemFromListTeacher(List<Teacher> list, Teacher item) {
        List<Teacher> tmpList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getFullName().equals(item.getFullName())) {
                tmpList.add(list.get(i));
            }
        }
        return tmpList;
    }

    ///----------------///
    private void setSubjects(List<Subject> subjects) {
        realm.beginTransaction();
        RealmResults<Subject> requestSubject = realm.where(Subject.class).findAll();
        List<Subject> tmpSubjectList = new ArrayList<>();
        for (int i = 0; i < requestSubject.size(); i++) {
            tmpSubjectList.add(requestSubject.get(i));
        }
        for (int i = 0; i < subjects.size(); i++) {
            if (containsSubject(tmpSubjectList, subjects.get(i))) {
                tmpSubjectList = removeItemFromListSubject(tmpSubjectList, subjects.get(i));
            } else {
                realm.copyToRealm(subjects.get(i));
            }
        }
        for (int i = 0; i < tmpSubjectList.size(); i++) {
            tmpSubjectList.get(i).deleteFromRealm();
        }
        realm.commitTransaction();
    }

    private boolean containsSubject(List<Subject> list, Subject item) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(item.getName())) {
                return true;
            }
        }
        return false;
    }

    private List<Subject> removeItemFromListSubject(List<Subject> list, Subject item) {
        List<Subject> tmpList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getName().equals(item.getName())) {
                tmpList.add(list.get(i));
            }
        }
        return tmpList;
    }

    ///----------------///
    private void setRooms(List<Room> rooms) {
        realm.beginTransaction();
        RealmResults<Room> requestRoom = realm.where(Room.class).findAll();
        List<Room> tmpRoomList = new ArrayList<>();
        for (int i = 0; i < requestRoom.size(); i++) {
            tmpRoomList.add(requestRoom.get(i));
        }
        for (int i = 0; i < rooms.size(); i++) {
            if (containsRoom(tmpRoomList, rooms.get(i))) {
                tmpRoomList = removeItemFromListRoom(tmpRoomList, rooms.get(i));
            } else {
                realm.copyToRealm(rooms.get(i));
            }
        }
        for (int i = 0; i < tmpRoomList.size(); i++) {
            tmpRoomList.get(i).deleteFromRealm();
        }
        realm.commitTransaction();
    }

    private boolean containsRoom(List<Room> list, Room item) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(item.getId())) {
                return true;
            }
        }
        return false;
    }

    private List<Room> removeItemFromListRoom(List<Room> list, Room item) {
        List<Room> tmpList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getName().equals(item.getName())) {
                tmpList.add(list.get(i));
            }
        }
        return tmpList;
    }

    ///----------------///
    private void setGroups(List<Group> groups) {
        realm.beginTransaction();
        RealmResults<Group> requestGroup = realm.where(Group.class).findAll();
//        requestGroup.deleteAllFromRealm();
//        for (int i = 0; i < groups.size(); i++) {
//            realm.copyToRealm(groups.get(i));
//        }
        List<Group> tmpGroupList = new ArrayList<>();
        for (int i = 0; i < requestGroup.size(); i++) {
            tmpGroupList.add(requestGroup.get(i));
        }
        for (int i = 0; i < groups.size(); i++) {
            if (containsGroup(tmpGroupList, groups.get(i))) {
                tmpGroupList = removeItemFromListGroup(tmpGroupList, groups.get(i));
            } else {
                realm.copyToRealm(groups.get(i));
            }
        }
        for (int i = 0; i < tmpGroupList.size(); i++) {
            tmpGroupList.get(i).deleteFromRealm();
        }
        realm.commitTransaction();
    }

    private boolean containsGroup(List<Group> list, Group item) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(item.getName())) {
                return true;
            }
        }
        return false;
    }

    private List<Group> removeItemFromListGroup(List<Group> list, Group item) {
        List<Group> tmpList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getName().equals(item.getName())) {
                tmpList.add(list.get(i));
            }
        }
        return tmpList;
    }
    ///----------------///
}
