package com.example.todolist.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolist.model.Tag;
import com.example.todolist.model.TaskItem;
import com.example.todolist.model.TaskLog;
import com.example.todolist.repository.TagRepository;
import com.example.todolist.repository.TaskLogRepository;
import com.example.todolist.repository.TaskRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskDayViewModel extends ViewModel {
    private final TaskRepository taskRepository = new TaskRepository();
    private final TagRepository tagRepository = new TagRepository();
    private final TaskLogRepository taskLogRepository = new TaskLogRepository();

    private final MutableLiveData<List<TaskItem>> _tasks = new MutableLiveData<>();
    public LiveData<List<TaskItem>> tasks = _tasks;

    private final MutableLiveData<List<Tag>> _tags = new MutableLiveData<>();
    public LiveData<List<Tag>> tags = _tags;

    private final MutableLiveData<List<TaskLog>> _logs = new MutableLiveData<>();
    public LiveData<List<TaskLog>> logs = _logs;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void loadData(String day, int userId){
        executor.execute(() -> {
            _tasks.postValue(taskRepository.getAllTask(day, userId));
            _tags.postValue(tagRepository.getAllTags());
            _logs.postValue(taskLogRepository.getAllTaskLogs());
        });
    }

    public void updateLogs(List<TaskLog> newLogs) {
        _logs.setValue(newLogs);
    }
}
