package com.example.todolist.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.databinding.ActivityLoginBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class LoginActivity extends AppCompatActivity{
    public static int userIdCurrent;
    private ActivityLoginBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private SharedPreferences preferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int user_id = preferences.getInt("user_id", 0);
        if(user_id != 0){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        binding.signupNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        binding.signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailEditText.getText().toString().trim();
                String password = binding.passwordEditText.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    loginUser(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Vui lÃ²ng nháº­p Äáº§y Äá»§ thÃ´ng tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(String username, String password){
//        Toast.makeText(LoginActivity.this, "http://localhost:8080/login", Toast.LENGTH_SHORT).show();
        JSONObject json = new JSONObject();
        try{
            json.put("username", username);
            json.put("password", password);
        } catch (Exception e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url("http://192.168.10.105:5000/user/login")
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

//        Log.d("Request :", request.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try{
                        String resStr = response.body().string();
                        JSONObject resJSON = new JSONObject(resStr);
                        boolean success = resJSON.getBoolean("success");

                        if(success){
                            preferences.edit().putInt("user_id", resJSON.getInt("user_id")).apply();
                            runOnUiThread(() -> {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            });
                        } else{
                            String msg = resJSON.getString("message");
                            runOnUiThread(() ->
                                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show()
                            );
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                        runOnUiThread(() ->
                                Toast.makeText(LoginActivity.this, "Lá»i xá»­ lÃ½ pháº£n há»i", Toast.LENGTH_SHORT).show()
                        );
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "Sai tÃ i khoáº£n hoáº·c máº­t kháº©u", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}