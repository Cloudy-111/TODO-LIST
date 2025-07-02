package com.example.todolist.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.todolist.R;
import com.example.todolist.Utils.DateTimeUtils;
import com.example.todolist.activities.LoginActivity;
import com.example.todolist.databinding.FragmentHomeBinding;
import com.example.todolist.databinding.FragmentProfileBinding;
import com.example.todolist.model.StatisticalResult;
import com.example.todolist.model.User;
import com.example.todolist.viewModel.ProfileViewModel;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private SharedPreferences preferences;
    private ProfileViewModel profileViewModel;
    private User user;
    private StatisticalResult stats;
    private TextView totalText, completeText, remainingText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", 0);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        totalText = binding.totalTaskTextview;
        completeText = binding.completedTaskTextview;
        remainingText = binding.remainTaskTextview;

        observeData();
        observeDataStatistic();

        profileViewModel.loadData(userId, DateTimeUtils.getTodayDate());
        profileViewModel.loadStatistics(userId, DateTimeUtils.getTodayDate());
        binding.logoutSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.edit().putInt("user_id", 0).apply();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
//                finish();
            }
        });
    }

    private void logoutAction(){
        preferences.edit().putInt("user_id", 0).apply();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void observeData(){
        profileViewModel.user.observe(getViewLifecycleOwner(), user -> {
            this.user = user;
            if(user != null){
                binding.profileEmail.setText(user.getEmail());
                binding.profileName.setText(user.getUsername());
                binding.profileEmailInfor.setText(user.getEmail());
                binding.profileNameInfor.setText(user.getUsername());
                Glide.with(binding.getRoot())
                        .load(user.getAvatar())
                        .placeholder((R.drawable.cat))
                        .error(R.drawable.ic_error)
                        .into(binding.profileAvatar);
            }
        });
    }

    private void observeDataStatistic(){
        profileViewModel.stats.observe(getViewLifecycleOwner(), stats -> {
            totalText.setText(String.valueOf(stats.getTotal()));
            completeText.setText(String.valueOf(stats.getComplete()));
            remainingText.setText(String.valueOf(stats.getRemaining()));
        });
    }
}