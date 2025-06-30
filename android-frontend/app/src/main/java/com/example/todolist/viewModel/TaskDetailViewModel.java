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

public class TaskDetailViewModel extends ViewModel {
    private final TaskRepository taskRepository = new TaskRepository();
    private final TagRepository tagRepository = new TagRepository();
    private final TaskLogRepository taskLogRepository = new TaskLogRepository();

    private final MutableLiveData<TaskItem> _task = new MutableLiveData<>();
    public LiveData<TaskItem> task = _task;
    private final MutableLiveData<Tag> _tag = new MutableLiveData<>();
    public LiveData<Tag> tag = _tag;
    private final MutableLiveData<TaskLog> _taskLog = new MutableLiveData<>();
    public LiveData<TaskLog> taskLog = _taskLog;

    public void loadData(int taskId){
        _task.setValue(taskRepository.getTaskById(taskId));
//        _tag.setValue(tagRepository.getTagByTaskId(taskId));
//        _taskLog.setValue(taskLogRepository.getTaskLogByTaskId(taskId));
    }
}
