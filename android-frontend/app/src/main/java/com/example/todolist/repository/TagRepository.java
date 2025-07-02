package com.example.todolist.repository;

import com.example.todolist.fake.fakeDB;
import com.example.todolist.model.Tag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TagRepository {
    private OkHttpClient client = new OkHttpClient();
    private final String baseURL = "http://192.168.10.105:5000";
    public interface AddNewTag{
        void onSuccess(int tagId);
        void onError(String errorMessage);
    }
    public List<Tag> getAllTags() {
        return fakeDB.getAllTag();
    }

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

    public void saveNewTag(Tag item){

    }
}