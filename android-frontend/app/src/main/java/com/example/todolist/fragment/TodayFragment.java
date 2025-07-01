package com.example.todolist.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.Utils.DateTimeUtils;
import com.example.todolist.activities.LoginActivity;
import com.example.todolist.activities.MainActivity;
import com.example.todolist.activities.TaskDetailActivity;
import com.example.todolist.adapter.CalendarAdapter;
import com.example.todolist.adapter.TaskAdapter;
import com.example.todolist.databinding.FragmentTodayBinding;
import com.example.todolist.model.Tag;
import com.example.todolist.model.TaskItem;
import com.example.todolist.model.TaskLog;

import com.example.todolist.Utils.DateTimeUtils.*;
import com.example.todolist.viewModel.TaskDayViewModel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TodayFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private FragmentTodayBinding binding;
    private RecyclerView recyclerViewToday;
    private RecyclerView recyclerViewCalendar;
    private TextView textTodayTitle;
    private TaskAdapter taskAdapter;
    private LocalDate selectedDate;
    private List<TaskLog> taskLogsToday = new ArrayList<>();
    private ArrayList<LocalDate> daysInWeek;
    private TaskDayViewModel viewModel;
    private SharedPreferences preferences;
    public TodayFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentTodayBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        int user_id = preferences.getInt("user_id", 0);
        viewModel = new ViewModelProvider(this).get(TaskDayViewModel.class);

        observeData();
        initCalendar();
        setWeekView();
        setupRecyclerView();

        viewModel.loadData(DateTimeUtils.getTodayDate(), user_id);
    }

    private void setupRecyclerView(){
        taskAdapter = new TaskAdapter(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), getContext(),  (task, isChecked) -> {
            List<TaskLog> currentLogs = new ArrayList<>(taskLogsToday);
            if (isChecked) {
                currentLogs.add(new TaskLog(currentLogs.size(), task.getId(), DateTimeUtils.getTodayDate(), ""));
            } else {
                Iterator<TaskLog> it = currentLogs.iterator();
                while (it.hasNext()) {
                    TaskLog log = it.next();
                    if (log.getTaskId() == task.getId() && log.getCompletedAt().equals(DateTimeUtils.getTodayDate())) {
                        it.remove();
                        break;
                    }
                }
            }
            viewModel.updateLogs(currentLogs);
        }, (task) -> {
            Intent intent = new Intent(getContext(), TaskDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("task_id", task.getId());
            intent.putExtras(bundle);
            startActivity(intent);
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(taskAdapter);
    }

    private void observeData(){
        viewModel.tasks.observe(getViewLifecycleOwner(), taskItems -> {
            taskAdapter.updateData(taskItems);
        });

        viewModel.tags.observe(getViewLifecycleOwner(), tags -> {
            taskAdapter.updateTags(tags);
        });

        viewModel.logs.observe(getViewLifecycleOwner(), logs -> {
            taskLogsToday = new ArrayList<>(logs);
            taskAdapter.updateLogs(logs);
        });
    }

    private void initCalendar(){
        selectedDate = LocalDate.now();
        recyclerViewCalendar = binding.calendarRecyclerView;
    }

    private void setWeekView(){
        daysInWeek = daysInWeekArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInWeek, selectedDate, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        recyclerViewCalendar.setLayoutManager(layoutManager);
        recyclerViewCalendar.setAdapter(calendarAdapter);
    }

    private ArrayList<LocalDate> daysInWeekArray(LocalDate date) {
        ArrayList<LocalDate> daysInWeekArray = new ArrayList<>();

        // Lấy ngày đầu tuần (Chủ nhật)
        LocalDate current = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        for (int i = 0; i < 7; i++) {
            daysInWeekArray.add(current.plusDays(i));
        }

        return daysInWeekArray;
    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        if (!dayText.equals("")) {
            selectedDate = daysInWeek.get(position);

            int user_id = preferences.getInt("user_id", 0);

            viewModel.loadData(selectedDate.toString(), user_id);
            setWeekView();
        }
    }
}