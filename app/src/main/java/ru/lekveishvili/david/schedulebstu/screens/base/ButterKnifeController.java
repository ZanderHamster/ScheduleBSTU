package ru.lekveishvili.david.schedulebstu.screens.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Контроллер, добавляющий функционал ButterKnife.
 * <p>
 * Дочерние контроллеры могут пользоваться View-полями начиная с метода {@link #onViewBound(View)}
 * <p>
 * При закрытии контроллера ({@link #onDestroyView(View)}) ссылки на view зануляются.
 */
abstract class ButterKnifeController extends Controller {

    private Unbinder unbinder;

    ButterKnifeController() {
    }

    ButterKnifeController(Bundle args) {
        super(args);
    }

    protected abstract View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container);

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflateView(inflater, container);
        unbinder = ButterKnife.bind(this, view);
        onViewBound(view);
        return view;
    }

    /**
     * Вызывается после создания View, когда ButterKnife привязал поля
     */
    protected void onViewBound(@NonNull View view) {
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        unbinder.unbind();
        unbinder = null;
    }
}
