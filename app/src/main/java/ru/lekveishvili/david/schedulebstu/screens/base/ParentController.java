package ru.lekveishvili.david.schedulebstu.screens.base;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import io.realm.RealmResults;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.ScheduleBSTUApplication;
import ru.lekveishvili.david.schedulebstu.models.Group;
import ru.lekveishvili.david.schedulebstu.models.Room;
import ru.lekveishvili.david.schedulebstu.network.RetrofitClient;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;
import ru.lekveishvili.david.schedulebstu.network.usecase.GetGroupUseCase;
import ru.lekveishvili.david.schedulebstu.network.usecase.GetRoomUseCase;
import ru.lekveishvili.david.schedulebstu.service.BottomNavigationService;
import ru.lekveishvili.david.schedulebstu.util.BundleBuilder;

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


    GetRoomUseCase getRoomUseCase;
    GetGroupUseCase getGroupUseCase;

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

    private void fetchData() {
        MainApiService apiService = RetrofitClient.getMainApiService();
        getGroupUseCase = new GetGroupUseCase(apiService);
        getGroupUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setGroups
                );
        getRoomUseCase = new GetRoomUseCase(apiService);
        getRoomUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setRooms
                );
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
            if (!list.get(i).getId().equals(item.getId())) {
                tmpList.add(list.get(i));
            }
        }
        return tmpList;
    }

    ///----------------///
    private void setGroups(List<Group> groups) {
        realm.beginTransaction();
        RealmResults<Group> requestGroup = realm.where(Group.class).findAll();
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
            if (list.get(i).getId().equals(item.getId())) {
                return true;
            }
        }
        return false;
    }

    private List<Group> removeItemFromListGroup(List<Group> list, Group item) {
        List<Group> tmpList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getId().equals(item.getId())) {
                tmpList.add(list.get(i));
            }
        }
        return tmpList;
    }

    ///----------------///

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
}
