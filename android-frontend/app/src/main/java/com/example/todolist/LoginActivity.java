package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.todolist.databinding.ActivityLoginBinding;
import com.example.todolist.databinding.ActivitySignupBinding;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity{
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signupNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
}
