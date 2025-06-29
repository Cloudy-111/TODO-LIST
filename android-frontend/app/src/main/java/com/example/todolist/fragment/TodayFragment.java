package com.example.todolist.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.Utils.DateTimeUtils;
import com.example.todolist.adapter.TaskAdapter;
import com.example.todolist.databinding.FragmentTodayBinding;
import com.example.todolist.model.Tag;
import com.example.todolist.model.TaskItem;
import com.example.todolist.model.TaskLog;

import com.example.todolist.Utils.DateTimeUtils.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TodayFragment extends Fragment {
    private FragmentTodayBinding binding;
    private RecyclerView recyclerViewToday;
    private TextView textTodayTitle;
    private TaskAdapter taskAdapter;
    private List<TaskLog> taskLogsToday = new ArrayList<>();
    public TodayFragment(){}
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentTodayBinding.inflate(getLayoutInflater());

        setupRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        loadTaskToday();
    }

    private void setupRecyclerView(){
        taskAdapter = new TaskAdapter(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), (task, isChecked) -> {
            if(isChecked){
                taskLogsToday.add(new TaskLog(taskLogsToday.size(), task.getId(), DateTimeUtils.getTodayDate(), ""));
            } else {
                Iterator<TaskLog> it = taskLogsToday.iterator();
                while (it.hasNext()) {
                    TaskLog log = it.next();
                    Log.d("Date Click: ", DateTimeUtils.getTodayDate());
                    if (log.getTaskId() == task.getId() && log.getCompletedAt().equals(DateTimeUtils.getTodayDate())) {
                        it.remove();
                        break;
                    }
                }
            }
            taskAdapter.updateLogs(taskLogsToday);
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(taskAdapter);
    }

    private void loadTaskToday(){
        taskLogsToday.add(new TaskLog(1, 3, "2025-06-30", ""));

        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag(1, "#F96060", "Work"));
        tagList.add(new Tag(2, "#6074F9", "Habit"));
        tagList.add(new Tag(3, "#313131", "Market"));
        tagList.add(new Tag(4, "#03DAC5", "Most Important"));
        tagList.add(new Tag(5, "#E0BBE4", "Oh Wow!"));

        List<TaskItem> list = new ArrayList<>();
        list.add(new TaskItem(1, "Completed Routine 1", "Time is Money!", "2025-06-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 1));
        list.add(new TaskItem(2, "Completed Routine 2", "Time is Money!", "2025-06-20", "2025-08-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 2));
        list.add(new TaskItem(3, "Completed Routine 3", "Time is Money!", "2025-06-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 3));
        list.add(new TaskItem(4, "Completed Routine 4", "Time is Money!", "2025-07-20", "2025-08-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 4));
        list.add(new TaskItem(5, "Completed Routine 5", "Time is Money!", "2025-06-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 5));
        list.add(new TaskItem(6, "Completed Routine 6", "Time is Money!", "2025-07-20", "2025-09-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 4));
        list.add(new TaskItem(7, "Completed Routine 7", "Time is Money!", "2025-07-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 1));
        list.add(new TaskItem(8, "Completed Routine 8", "Time is Money!", "2025-07-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 4));
        list.add(new TaskItem(9, "Completed Routine 9", "Time is Money!", "2025-06-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 3));
        list.add(new TaskItem(10, "Completed Routine 10", "Time is Money!", "2025-07-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 1));
        list = DateTimeUtils.filterTaskToday(list);

        taskAdapter.updateData(list);
        taskAdapter.updateTags(tagList);
        taskAdapter.updateLogs(taskLogsToday);
    }
}