package com.example.todolist.viewModel;

import androidx.lifecycle.ViewModel;

import com.example.todolist.model.TaskItem;
import com.example.todolist.repository.TaskRepository;

public class AddTaskViewModel extends ViewModel {
    private final TaskRepository taskRepository = new TaskRepository();

    public void saveNewTask(TaskItem task, TaskRepository.AddTaskCallback callback){
        taskRepository.saveNewTask(task, callback);
    }
}
