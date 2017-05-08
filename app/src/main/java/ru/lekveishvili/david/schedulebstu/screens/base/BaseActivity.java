package ru.lekveishvili.david.schedulebstu.screens.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.lekveishvili.david.schedulebstu.ScheduleBSTUApplication;
import ru.lekveishvili.david.schedulebstu.di.components.AppComponent;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent(ScheduleBSTUApplication.getAppComponent(this));
    }

    protected abstract void setupActivityComponent(AppComponent appComponent);
}
