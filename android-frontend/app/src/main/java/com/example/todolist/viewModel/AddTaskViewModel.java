package com.example.todolist.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolist.model.Tag;
import com.example.todolist.model.TaskItem;
import com.example.todolist.repository.TagRepository;
import com.example.todolist.repository.TaskRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddTaskViewModel extends ViewModel {
    private final TaskRepository taskRepository = new TaskRepository();
    private final TagRepository tagRepository = new TagRepository();

    private MutableLiveData<List<Tag>> _tagList =  new MutableLiveData<>();
    public LiveData<List<Tag>> tagList = _tagList;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    public void saveNewTask(TaskItem task, TaskRepository.AddTaskCallback callback){
        taskRepository.saveNewTask(task, callback);
    }
    public void saveNewTag(Tag tag, TagRepository.AddNewTagCallback callback){
        tagRepository.saveNewTag(tag, callback);
    }

    public void loadAllTagsByUserId(int userId){
        executor.execute(() -> {
            _tagList.postValue(tagRepository.getAllTagsById(userId));
        });
    }
}
