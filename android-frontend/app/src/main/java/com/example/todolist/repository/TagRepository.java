package com.example.todolist.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.todolist.fake.fakeDB;
import com.example.todolist.model.Tag;

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

public class TagRepository {
    private OkHttpClient client = new OkHttpClient();
    private final String baseURL = "http://192.168.10.105:5000";
    public interface AddNewTagCallback{
        void onSuccess(Tag newTag);
        void onError(String errorMessage);
    }
//    public List<Tag> getAllTags() {
//        return fakeDB.getAllTag();
//    }

    public List<Tag> getAllTagsById(int userId){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL + "/tag/getUserId/" + String.valueOf(userId)).newBuilder();

        String url = urlBuilder.toString();

        List<Tag> result = new ArrayList<>();
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
                    Tag item = new Tag(
                            objects.getInt("id"),
                            objects.getInt("createBy"),
                            objects.getString("color"),
                            objects.getString("name")
                    );
                    result.add(item);
                }
            }
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return result;
    }

    public void saveNewTag(Tag item, AddNewTagCallback callback){
        String url = baseURL + "/tag/add";
        JSONObject json = new JSONObject();
        try {
            json.put("name", item.getName());
            json.put("color", item.getColor());
            json.put("createdBy", item.getUserId());
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
                        Log.d("JSON", resJSON.toString());

                        // Xử lý dữ liệu
                        boolean success = resJSON.getBoolean("success");
                        if (success) {
                            JSONObject data = resJSON.getJSONObject("data");
                            int id = data.getInt("id");
                            String name = data.getString("name");
                            String color = data.getString("color");
                            int userId = data.getInt("createdBy");

                            Tag newTag = new Tag(id, userId, color, name);
                            callback.onSuccess(newTag);
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