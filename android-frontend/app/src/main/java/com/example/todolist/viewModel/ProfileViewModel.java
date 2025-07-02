package com.example.todolist.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolist.model.User;
import com.example.todolist.repository.UserRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileViewModel extends ViewModel {
    private final UserRepository userRepository = new UserRepository();
    private final MutableLiveData<User> _user = new MutableLiveData<>();
    public final LiveData<User> user = _user;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void loadData(int userId){
        executorService.execute(() -> {
            _user.postValue(userRepository.getUserById(userId));
        });
    }
}