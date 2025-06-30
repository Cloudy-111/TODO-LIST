package com.example.todolist.repository;

import androidx.annotation.NonNull;

import com.example.todolist.fake.fakeDB;
import com.example.todolist.model.TaskItem;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TaskRepository {
    private final OkHttpClient client = new OkHttpClient();

    public interface AddTaskCallback{
        void onSuccess(int userId);
        void onError(String errorMessage);
    }

    public List<TaskItem> getAllTasks() {
        return fakeDB.getAllTask();
    }

    public TaskItem getTaskById(int taskId) {
        return fakeDB.getTaskItem(taskId);
    }

    public void saveNewTask(TaskItem item, AddTaskCallback callback){
        JSONObject json = new JSONObject();
        try {
            json.put("name", item.getName());
            json.put("description", item.getDescription());
            json.put("create_at", item.getStartDate());
            json.put("end_at", item.getEndDate());
            json.put("due_time", item.getDue_time());
            json.put("image", item.getImage_task());
            json.put("user_id", item.getUserId());
            json.put("tag_id", item.getTagId());
        } catch (Exception e){
            e.printStackTrace();
            callback.onError("Lỗi tạo JSON");
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url("http://107.98.86.164:5000/task/add/")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Không thể kết nối tới máy chủ");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String resStr = response.body().string();
                        JSONObject resJSON = new JSONObject(resStr);
                        boolean success = resJSON.getBoolean("success");
                        if (success) {
                            int userId = resJSON.getInt("user_id");
                            callback.onSuccess(userId);
                        } else {
                            callback.onError(resJSON.getString("message"));
                        }
                    } catch (Exception e) {
                        callback.onError("Lỗi xử lý phản hồi");
                    }
                } else {
                    callback.onError("Lỗi từ máy chủ");
                }
            }
        });
    }
}
