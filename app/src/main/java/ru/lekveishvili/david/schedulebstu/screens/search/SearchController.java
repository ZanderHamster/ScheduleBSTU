package ru.lekveishvili.david.schedulebstu.screens.search;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.models.Teacher;
import ru.lekveishvili.david.schedulebstu.screens.base.BaseController;
import ru.lekveishvili.david.schedulebstu.screens.search.adapters.NothingSelectedSpinnerAdapter;

public class SearchController extends BaseController {
    @BindView(R.id.search_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.search_tabhost)
    TabHost tabHost;

    @BindView(R.id.search_groups_spinner)
    Spinner groupsSpinner;
    @BindView(R.id.search_teacher_spinner)
    Spinner teacherSpinner;

    @BindView(R.id.search_teacher_spinner_reset)
    ImageView resetTeacher;

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
        configureList();
        configureSpinnerGroups();
        configureSpinnerTeachers();
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
    }

    private void configureSpinnerGroups() {

        List<String> previouslyInfoList = new ArrayList<>();
        previouslyInfoList.add("test1");
        previouslyInfoList.add("test2");
        previouslyInfoList.add("test3");
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                previouslyInfoList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupsSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter,
                        R.layout.item_spinner_row_nothing_selected_groups,
                        getActivity()));
    }

    private void configureSpinnerTeachers() {
        List<String> teachersList = new ArrayList<>();
        RealmResults<Teacher> teachers = realm.where(Teacher.class)
                .findAll();
        for (int i = 0; i < teachers.size(); i++) {
            teachersList.add(teachers.get(i).getFullName());
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
                    setVisibilityResetTeacher(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        resetTeacher.setOnClickListener(v -> {
            teacherSpinner.setSelection(0);
            setVisibilityResetTeacher(false);
        });
    }

    private void setVisibilityResetTeacher(boolean flag) {
        if (flag) {
            resetTeacher.setVisibility(View.VISIBLE);
        } else {
            resetTeacher.setVisibility(View.GONE);
        }
    }

    private void configureList() {
    }

    private void configureToolbar() {
        toolbarTitle.setText(getResources().getString(R.string.bottom_menu_search));
    }
}
