package ru.lekveishvili.david.schedulebstu.screens.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.ChangeHandlerFrameLayout;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import butterknife.BindView;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.screens.account.AccountController;
import ru.lekveishvili.david.schedulebstu.screens.home.HomeController;
import ru.lekveishvili.david.schedulebstu.screens.search.SearchController;
import ru.lekveishvili.david.schedulebstu.util.BundleBuilder;

/** Контроллер для хранения роутера для каждого пункта нижнего меню */
public class ParentSubController extends BaseController{
    private static final String KEY_TAG = "ParentSubController.tag";
    @BindView(R.id.controller_container)
    ChangeHandlerFrameLayout controllerContainer;
    private ParentController.Tag tag;
    private Router insideBottomCategoryRouter;


    public ParentSubController(ParentController.Tag tag) {
        this(new BundleBuilder(new Bundle())
                .putString(KEY_TAG, tag.toString())
                .build()
        );
    }

    public ParentSubController(Bundle args) {
        super(args);
        String defaultTag = ParentController.Tag.HOME.toString();
        String tagStr = getArgs().getString(KEY_TAG, defaultTag);
        this.tag = ParentController.Tag.valueOf(tagStr);
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_parent_sub, container, false);
    }
    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        Activity activity = getActivity();
        if (activity == null) return;
        setRouterRootIfNeeded();
    }

    private void setRouterRootIfNeeded() {
        if (insideBottomCategoryRouter == null) {
            insideBottomCategoryRouter = getChildRouter(controllerContainer);
        }

        if (insideBottomCategoryRouter.hasRootController()) {
            return;
        }

        Controller controller = null;

        switch (tag) {
            case HOME:
                controller = new HomeController();
                break;
            case SEARCH:
                controller = new SearchController();
                break;
            case ACCOUNT:
                controller = new AccountController();
                break;
            default:
                break;
        }

        insideBottomCategoryRouter.setRoot(RouterTransaction.with(controller));
    }

    Router getChildRouter() {
        if (insideBottomCategoryRouter == null) {
            insideBottomCategoryRouter = getChildRouter(controllerContainer, tag.toString());
        }
        return insideBottomCategoryRouter;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TAG, tag.toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String defaultTag = ParentController.Tag.HOME.toString();
        String tagStr = savedInstanceState.getString(KEY_TAG, defaultTag);
        this.tag = ParentController.Tag.valueOf(tagStr);
    }
}
