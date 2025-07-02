package com.example.todolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.databinding.ActivitySignupBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private final String image_avatar = "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signinNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.usernameEditText.getText().toString().trim();
                String email = binding.emailEditText.getText().toString().trim();
                String password = binding.passwordEditText.getText().toString().trim();

                if(!username.isEmpty() || !password.isEmpty()){
                    signupUser(username, password, email, image_avatar);
                } else {
                    Toast.makeText(SignupActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signupUser(String username, String password, String email, String image_avatar){
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://192.168.10.105:5000/user/register";

        JSONObject json = new JSONObject();
        try{
            json.put("username", username);
            json.put("password", password);
            json.put("email", email);
            json.put("avatar", image_avatar);
        } catch (Exception e){
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();

        Log.d("JSON: ", json.toString());
        RequestBody body = RequestBody.create(MEDIA_TYPE, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("JSON: ", "DIT");
                runOnUiThread(() -> {
                    Toast.makeText(SignupActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
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
                            int userId = resJSON.getInt("user_id");
                            runOnUiThread(() -> {
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                intent.putExtra("user_id", userId);  // truyền dữ liệu
                                startActivity(intent);
                                finish();
                            });
                        } else{
                            String msg = resJSON.getString("message");
                            runOnUiThread(() ->
                                    Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show()
                            );
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                        runOnUiThread(() ->
                                Toast.makeText(SignupActivity.this, "Lỗi xử lý phản hồi", Toast.LENGTH_SHORT).show()
                        );
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(SignupActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}