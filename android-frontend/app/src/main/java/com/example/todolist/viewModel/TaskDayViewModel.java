package com.example.todolist.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolist.model.Tag;
import com.example.todolist.model.TaskItem;
import com.example.todolist.model.TaskLog;
import com.example.todolist.repository.TagRepository;
import com.example.todolist.repository.TaskLogRepository;
import com.example.todolist.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskDayViewModel extends ViewModel {
    private TaskRepository taskRepository = new TaskRepository();
    private TagRepository tagRepository = new TagRepository();
    private TaskLogRepository taskLogRepository = new TaskLogRepository();
    private MutableLiveData<List<TaskItem>> _taskList = new MutableLiveData<>();
    public LiveData<List<TaskItem>> taskList = _taskList;
    private MutableLiveData<List<Tag>> _tagList = new MutableLiveData<>();
    public LiveData<List<Tag>> tagList = _tagList;
    private  MutableLiveData<List<TaskLog>> _taskLogList = new MutableLiveData<>();
    public LiveData<List<TaskLog>> taskLogList = _taskLogList;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void loadData(String day, int userId){
        executor.execute(() -> {
            _taskList.postValue(taskRepository.getAllTask(day, userId));
            _tagList.postValue(tagRepository.getAllTagsById(userId));

            List<TaskLog> logs = taskLogRepository.getAllTaskLogsByUserId(userId, day);

            List<TaskLog> todayCheckedLogs = new ArrayList<>();
            for (TaskLog log : logs) {
                Log.d("Date TaskLog", log.getCompletedAt());
                if (log.getCompletedAt().equals(day) && log.getStatus()) {
                    todayCheckedLogs.add(log);
                }
            }
            _taskLogList.postValue(todayCheckedLogs);
        });
    }

    public void checkTask(int taskId, String date){
        taskLogRepository.updateTaskLogStatus(taskId, date);
    }

    public void updateLogs(List<TaskLog> newLogs){
        _taskLogList.setValue(newLogs);
    }
}
