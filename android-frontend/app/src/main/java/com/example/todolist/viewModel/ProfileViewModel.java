package com.example.todolist.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolist.model.StatisticalResult;
import com.example.todolist.model.User;
import com.example.todolist.repository.UserRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProfileViewModel extends ViewModel {
    private final UserRepository userRepository = new UserRepository();
    private final MutableLiveData<User> _user = new MutableLiveData<>();
    public final LiveData<User> user = _user;
    private final MutableLiveData<StatisticalResult> _stats = new MutableLiveData<>();
    public LiveData<StatisticalResult> stats = _stats;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void loadData(int userId, String date){
        executorService.execute(() -> {
            _user.postValue(userRepository.getUserById(userId));
        });
    }

    public void loadStatistics(int userId, String date) {
        userRepository.getStatistical(userId, date, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace(); // hoặc xử lý lỗi
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        if (json.getBoolean("success")) {
                            int total = json.getInt("total");
                            int complete = json.getInt("complete");

                            StatisticalResult result = new StatisticalResult(total, complete);
                            _stats.postValue(result);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}