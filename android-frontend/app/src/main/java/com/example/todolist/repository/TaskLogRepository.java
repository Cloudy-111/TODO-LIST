package com.example.todolist.repository;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.todolist.Utils.DateTimeUtils;
import com.example.todolist.fake.fakeDB;
import com.example.todolist.model.TaskItem;
import com.example.todolist.model.TaskLog;

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

public class TaskLogRepository {
    private final OkHttpClient client = new OkHttpClient();
    private final String baseURL = "http://192.168.10.105:5000";
    public List<TaskLog> getAllTaskLogs() {
        return fakeDB.getAllTaskLog();
    }

    public List<TaskLog> getAllTaskLogsByUserId(int userId, String day){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL + "/taskLogs/" + userId + "/" + day).newBuilder();
        String url = urlBuilder.toString();

        List<TaskLog> result = new ArrayList<>();
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
                    TaskLog item = new TaskLog(
                            objects.getInt("id"),
                            objects.getInt("task_id"),
                            objects.getString("date"),
                            objects.getString("note"),
                            objects.getBoolean("status")
                    );
                    result.add(item);
                }
            }
            Log.d("TaskLog", result.get(0).toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void updateTaskLogStatus(int taskId, String date){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL + "/taskLog/update/" + taskId + "/" + date).newBuilder();
        String url = urlBuilder.toString();


        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("checkTask", "Failed to send request");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    try {
                        JSONObject resJson = new JSONObject(responseStr);
                        int id = resJson.getInt("id");
                        int taskId = resJson.getInt("taskId");
                        String completedAt = resJson.getString("date");
                        String note = resJson.getString("note");
                        Boolean status = resJson.getBoolean("status");

                        TaskLog newLog = new TaskLog(id, taskId, completedAt, note, status);
                        Log.d("Update Log: ", newLog.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("checkTask", "Request failed: " + response.code());
                }
            }
        });
    }
}
