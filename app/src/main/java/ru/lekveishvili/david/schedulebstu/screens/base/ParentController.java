package ru.lekveishvili.david.schedulebstu.screens.base;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.ScheduleBSTUApplication;
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
        Activity activity = getActivity();
        if (activity == null) return;
        ScheduleBSTUApplication.getAppComponent(activity)
                .inject(this);

        bottomNavigationRouter = getChildRouter(parentSubControllersContainer);
        setRouterRootIfNeeded();
        configureBottomNavMenu();
        subscribeToBottomNavigationState();
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
            switch (item.getItemId()){
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
