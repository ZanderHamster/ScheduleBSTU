package ru.lekveishvili.david.schedulebstu.screens.search;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.conductor.RouterTransaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.models.Group;
import ru.lekveishvili.david.schedulebstu.models.Teacher;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;
import ru.lekveishvili.david.schedulebstu.screens.search.adapters.NothingSelectedSpinnerAdapter;
import ru.lekveishvili.david.schedulebstu.screens.search_pager.SearchPagerController;

public class SearchController extends BaseController {
    private boolean isSetTeacher = false;
    private boolean isSetGroup = false;
    private boolean isSetDate = false;

    private String teacherDate = "";
    private String teacherName = "";
    private String groupDate = "";
    private String groupName = "";

    @BindView(R.id.search_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.search_tabhost)
    TabHost tabHost;

    @BindView(R.id.search_groups_spinner)
    Spinner groupsSpinner;
    @BindView(R.id.search_teacher_spinner)
    Spinner teacherSpinner;

    @BindView(R.id.search_teacher_date_layout)
    LinearLayout teacherDateLayout;
    @BindView(R.id.search_teacher_date_text)
    TextView tvTeacherDate;
    @BindView(R.id.search_group_date_layout)
    LinearLayout groupDateLayout;
    @BindView(R.id.search_group_date_text)
    TextView tvGroupDate;

    @BindView(R.id.search_teacher_spinner_reset)
    ImageView resetTeacher;
    @BindView(R.id.search_group_spinner_reset)
    ImageView resetGroup;

    @BindView(R.id.search_teacher_date_reset)
    ImageView resetTeacherDate;
    @BindView(R.id.search_teacher_date_icon)
    ImageView ivTeacherDateIcon;
    @BindView(R.id.search_group_date_reset)
    ImageView resetGroupDate;
    @BindView(R.id.search_group_date_icon)
    ImageView ivGroupDateIcon;

    @BindView(R.id.search_button)
    Button btnSearch;

    private Realm realm;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_search, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        realm = Realm.getDefaultInstance();
        configureToolbar();

        configureTabs();
        configureSpinnerGroups();
        configureSpinnerTeachers();
        configureDatePicker();

        configureSearch();
    }

    private void configureSearch() {
        btnSearch.setOnClickListener(v -> {
            if (isSetDate && isSetTeacher) {
                Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
                getRouter().pushController(RouterTransaction.with(
                        new SearchPagerController(teacherName, teacherDate, "Преподаватели")));
            }
            if (isSetDate && isSetGroup) {
                Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
                getRouter().pushController(RouterTransaction.with(new SearchPagerController(groupName, groupDate, "Группы")));
            }
        });
    }

    private void configureDatePicker() {
        teacherDateLayout.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(
                    v.getContext(),
                    (view, year, month, dayOfMonth) -> setDateTeacher(year, month, dayOfMonth),
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
        groupDateLayout.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(
                    v.getContext(),
                    (view, year, month, dayOfMonth) -> setDateGroup(year, month, dayOfMonth),
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    private void setDateTeacher(int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        month += 1;
        setVisibilityResetTeacherDate(true);
        String strMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
        if (month < 10) {
            strMonth = 0 + strMonth;
        }
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String strDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        if (day < 10) {
            strDay = 0 + strDay;
        }
        tvTeacherDate.setText(strDay + "/" + strMonth + "/" + year);
        teacherDate = year + "-" + strMonth + "-" + strDay;
        resetTeacherDate.setOnClickListener(v -> {
            teacherDate = "";
            setVisibilityResetTeacherDate(false);
        });
    }

    private void setDateGroup(int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        month += 1;
        setVisibilityResetGroupDate(true);
        String strMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
        if (month < 10) {
            strMonth = 0 + strMonth;
        }
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String strDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        if (day < 10) {
            strDay = 0 + strDay;
        }
        tvGroupDate.setText(strDay + "/" + strMonth + "/" + year);
        groupDate = year + "-" + strMonth + "-" + strDay;
        resetGroupDate.setOnClickListener(v -> {
            groupDate = "";
            setVisibilityResetGroupDate(false);
        });
    }

    private void configureTabs() {
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1")
                .setIndicator("Группы")
                .setContent(R.id.search_groups_tab);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2")
                .setIndicator("Преподаватели")
                .setContent(R.id.search_teachers_tab);
        tabHost.addTab(tabSpec);
        tabHost.setOnTabChangedListener(tabId -> {
            if (tabId == "tag1") {
                teacherSpinner.setSelection(0);
                setVisibilityResetTeacher(false);
                setVisibilityResetTeacherDate(false);
            } else {
                groupsSpinner.setSelection(0);
                setVisibilityResetGroup(false);
                setVisibilityResetGroupDate(false);
            }
        });
    }

    private void configureSpinnerGroups() {

        List<String> groupsList = new ArrayList<>();
        RealmResults<Group> groups = realm.where(Group.class)
                .findAll();
        for (int i = 0; i < groups.size(); i++) {
            if (!groupsList.contains(groups.get(i).getName())) {
                groupsList.add(groups.get(i).getName());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                groupsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupsSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter,
                        R.layout.item_spinner_row_nothing_selected_groups,
                        getActivity()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupsSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter,
                        R.layout.item_spinner_row_nothing_selected_groups,
                        getActivity()));
        groupsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    groupName = groupsList.get(position - 1);
                    setVisibilityResetGroup(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        resetGroup.setOnClickListener(v -> {
            groupsSpinner.setSelection(0);
            setVisibilityResetGroup(false);
        });
    }

    private void configureSpinnerTeachers() {
        List<String> teachersList = new ArrayList<>();
        RealmResults<Teacher> teachers = realm.where(Teacher.class)
                .findAll();
        for (int i = 0; i < teachers.size(); i++) {
            if (!teachersList.contains(teachers.get(i).getFullName())) {
                teachersList.add(teachers.get(i).getFullName());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                teachersList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teacherSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter,
                        R.layout.item_spinner_row_nothing_selected_teacher,
                        getActivity()));
        teacherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    teacherName = teachersList.get(position - 1);
                    setVisibilityResetTeacher(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        resetTeacher.setOnClickListener(v -> {
            teacherName = "";
            teacherSpinner.setSelection(0);
            setVisibilityResetTeacher(false);
        });
    }

    private void setVisibilityResetTeacher(boolean flag) {
        if (flag) {
            isSetTeacher = true;
            resetTeacher.setVisibility(View.VISIBLE);
        } else {
            isSetTeacher = false;
            resetTeacher.setVisibility(View.GONE);
        }
    }

    private void setVisibilityResetGroup(boolean flag) {
        if (flag) {
            isSetGroup = true;
            resetGroup.setVisibility(View.VISIBLE);
        } else {
            isSetGroup = false;
            resetGroup.setVisibility(View.GONE);
        }
    }

    private void setVisibilityResetTeacherDate(boolean flag) {
        if (flag) {
            isSetDate = true;
            resetTeacherDate.setVisibility(View.VISIBLE);
            ivTeacherDateIcon.setVisibility(View.GONE);
            tvTeacherDate.setTextColor(getResources().getColor(R.color.black));

        } else {
            isSetDate = false;
            tvTeacherDate.setText("Выбрать дату");
            tvTeacherDate.setTextColor(getResources().getColor(R.color.teacher_name_color));
            ivTeacherDateIcon.setVisibility(View.VISIBLE);
            resetTeacherDate.setVisibility(View.GONE);
        }
    }

    private void setVisibilityResetGroupDate(boolean flag) {
        if (flag) {
            isSetDate = true;
            resetGroupDate.setVisibility(View.VISIBLE);
            ivGroupDateIcon.setVisibility(View.GONE);
            tvGroupDate.setTextColor(getResources().getColor(R.color.black));
        } else {
            isSetDate = false;
            tvGroupDate.setText("Выбрать дату");
            tvGroupDate.setTextColor(getResources().getColor(R.color.teacher_name_color));
            ivGroupDateIcon.setVisibility(View.VISIBLE);
            resetGroupDate.setVisibility(View.GONE);
        }
    }

    private void configureToolbar() {
        toolbarTitle.setText(getResources().getString(R.string.bottom_menu_search));
    }
}