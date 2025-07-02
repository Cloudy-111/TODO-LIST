package com.example.todolist.repository;

import com.example.todolist.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserRepository {
    private final OkHttpClient client = new OkHttpClient();
    private final String baseURL = "http://192.168.10.105:5000";

    public User getUserById(int userId){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL + "/user/get/" + userId).newBuilder();
        String url = urlBuilder.toString();

        User result = new User();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resStr = response.body().string();
                JSONObject resJSON = new JSONObject(resStr);
                result.setId(resJSON.getInt("id"));
                result.setUsername(resJSON.getString("username"));
                result.setAvatar(resJSON.getString("avatar"));
                result.setEmail(resJSON.getString("email"));
            }
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return result;
    }
}