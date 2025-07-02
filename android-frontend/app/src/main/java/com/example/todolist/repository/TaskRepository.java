package com.example.todolist.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.todolist.fake.fakeDB;
import com.example.todolist.model.TaskItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TaskRepository {
    private final OkHttpClient client = new OkHttpClient();
    private final String baseURL = "http://192.168.10.105:5000";

    public interface AddTaskCallback{
        void onSuccess(String successMessage, TaskItem taskItem);
        void onError(String errorMessage);
    }

    public List<TaskItem> getAllTask(String day, int userId) {
//        String baseURL = "http://10.0.2.2:5000";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL + "/task/get/" + day + "/" + userId).newBuilder();
        String url = urlBuilder.toString();

        List<TaskItem> result = new ArrayList<>();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resStr = response.body().string();
                JSONArray resJSON = new JSONArray(resStr);
                for (int i = 0; i < resJSON.length(); i++) {
                    JSONObject objects = resJSON.getJSONObject(i);
                    TaskItem item = new TaskItem(
                            objects.getInt("id"),
                            objects.getInt("user_id"),
                            objects.getString("name"),
                            objects.getString("description"),
                            objects.getString("create_at"),
                            objects.getString("end_at"),
                            objects.getString("due_time"),
                            objects.getString("image"),
                            objects.getInt("tag_id")
                    );
                    result.add(item);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public TaskItem getTaskById(int taskId){
//        String baseURL = "http://10.0.2.2:5000";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL + "/task/get/" + String.valueOf(taskId)).newBuilder();

        String url = urlBuilder.toString();

        TaskItem result = new TaskItem(0, 0, "", "", "", "", "" ,"", 1);
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resStr = response.body().string();
                JSONObject resJSON = new JSONObject(resStr);
                result.setId(resJSON.getInt("id"));
                result.setUserId(resJSON.getInt("user_id"));
                result.setName(resJSON.getString("name"));
                result.setDescription(resJSON.getString("description"));
                result.setStartDate(resJSON.getString("create_at"));
                result.setEndDate(resJSON.getString("end_at"));
                result.setDue_time(resJSON.getString("due_time"));
                result.setImage_task(resJSON.getString("image"));
                result.setTagId(resJSON.getInt("id"));
            }
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return result;
    }

    public void saveNewTask(TaskItem item, AddTaskCallback callback){
        String url = baseURL + "/task/add/";
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
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Không thể kết nối tới máy chủ");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Đọc nội dung JSON string
                        String resStr = response.body().string();
                        Log.d("HTTP_RESPONSE", "Raw response: " + resStr);

                        // Parse JSON
                        JSONObject resJSON = new JSONObject(resStr);
                        boolean success = resJSON.getBoolean("success");
                        String message = resJSON.getString("message");
                        if (success) {
                            callback.onSuccess(message, item);
                        } else {
                            callback.onError(resJSON.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onError("Lỗi xử lý phản hồi: " + e.getMessage());
                    }
                } else {
                    callback.onError("Lỗi từ máy chủ: " + response.code());
                }
            }
        });
    }
}
