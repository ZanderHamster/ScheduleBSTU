package ru.lekveishvili.david.schedulebstu.screens.edit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.ScheduleBSTUApplication;
import ru.lekveishvili.david.schedulebstu.SessionService;
import ru.lekveishvili.david.schedulebstu.network.RetrofitClient;
import ru.lekveishvili.david.schedulebstu.network.models.EditEventRequest;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;
import ru.lekveishvili.david.schedulebstu.network.usecase.GetEditEventDataUseCase;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;
import ru.lekveishvili.david.schedulebstu.util.BundleBuilder;

public class EditController extends BaseController {
    MainApiService apiService = RetrofitClient.getMainApiService();
    private static final String KEY_ID = "ID";
    private static final String KEY_DATE = "DATE";
    private String id;
    private String dateOld;

    @BindView(R.id.edit_event_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.edit_event_toolbar_back_button)
    ImageView btnBack;

    @BindView(R.id.edit_event_date_hint)
    LinearLayout layoutDate;
    @BindView(R.id.edit_event_date_hint_text)
    TextView tvDate;
    @BindView(R.id.edit_event_date_hint_image)
    ImageView hintImageDate;
    @BindView(R.id.edit_event)
    Button btnEdit;

    @Inject
    SessionService sessionService;


    public EditController(String id, String dateOld) {
        this(new BundleBuilder(new Bundle())
                .putString(KEY_ID, id)
                .putString(KEY_DATE, dateOld)
                .build()
        );
    }

    @SuppressWarnings("WeakerAccess")
    public EditController(Bundle args) {
        super(args);
        id = getArgs().getString(KEY_ID, "");
        dateOld = getArgs().getString(KEY_DATE, "");

    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        ScheduleBSTUApplication.getAppComponent(view.getContext())
                .inject(this);

        configureToolbar();
        configureDate();
        configureEdit();
    }

    private void configureEdit() {
        btnEdit.setOnClickListener(v -> {
            EditEventRequest editEventRequest = new EditEventRequest();
            editEventRequest.id = this.id;
            editEventRequest.date = this.dateOld;
            editEventRequest.newDate = tvDate.getText().toString();

            new GetEditEventDataUseCase(apiService, sessionService.getToken())
                    .execute(editEventRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setModel,
                            throwable -> Log.d("NetworkError", throwable.getMessage()));

        });
    }

    private void setModel(boolean moved) {
        if (moved) {
            Snackbar.make(btnEdit, "Событие перенесено!",
                    Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(btnEdit, "Ошибка переноса!",
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    private void configureDate() {
        String[] split = dateOld.split("-");
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, Integer.valueOf(split[0]));
        myCalendar.set(Calendar.MONTH, Integer.valueOf(split[1]) - 1);
        myCalendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(split[2]));
        tvDate.setText(dateOld);
        tvDate.setTextColor(getResources().getColor(R.color.black));
        layoutDate.setOnClickListener(v -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", new Locale("ru", "RU"));
            DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tvDate.setText(sdf.format(myCalendar.getTime()));
                tvDate.setTextColor(getResources().getColor(R.color.black));

            };
            new DatePickerDialog(v.getContext(), date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }


    private void configureToolbar() {
        btnBack.setOnClickListener(v -> getRouter().handleBack());
        toolbarTitle.setText("Перенос");
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_edit_event, container, false);
    }
}
