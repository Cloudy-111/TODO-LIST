package com.example.todolist.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolist.R;
import com.example.todolist.Utils.DateTimeUtils;
import com.example.todolist.databinding.ActivityTaskDetailBinding;
import com.example.todolist.fake.fakeDB;
import com.example.todolist.model.TaskItem;
import com.example.todolist.model.TaskLog;
import com.example.todolist.repository.TaskRepository;
import com.example.todolist.viewModel.TaskDetailViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskDetailActivity extends AppCompatActivity {
    private TaskDetailViewModel taskDetailViewModel;
    private TaskItem item;
    private TaskLog log;
    private ActivityTaskDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        taskDetailViewModel = new ViewModelProvider(this).get(TaskDetailViewModel.class);

        final Observer<TaskItem> itemObserver = new Observer<TaskItem>() {
            @Override
            public void onChanged(TaskItem taskItem) {
                binding.titleTextView.setText(taskItem.getName());
                binding.startDateTextView.setText(taskItem.getStartDate());
                binding.endDateTextView.setText(taskItem.getEndDate());
                binding.descriptionTextView.setText(taskItem.getDescription());
//                binding.statusTextView.setText(log.getStatus() == true ? "Đã hoàn thành" : "Chưa hoàn thành");
            }
        };

        taskDetailViewModel.task.observe(this, itemObserver);

        taskDetailViewModel.taskLog.observe(this, new Observer<TaskLog>() {
            @Override
            public void onChanged(TaskLog taskLog) {
                log = taskLog;
                // Nếu cần hiển thị trạng thái:
                if (log != null) {
                    binding.statusTextView.setText(log.getStatus() ? "Đã hoàn thành" : "Chưa hoàn thành");
                    binding.taskNoteText.setText(log.getNote());
                }
            }
        });

        //Retrieve Data
        Bundle bundle = getIntent().getExtras();
        int taskId = bundle.getInt("task_id");
        taskDetailViewModel.loadData(taskId, DateTimeUtils.getTodayDate());

        // Xử lý khoảng cách hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.backButton.setOnClickListener(v -> finish());
        binding.saveButton.setOnClickListener(v -> {
            String noteText = binding.taskNoteText.getText().toString();
            log.setNote(noteText);
            saveEditTask(item, log);
        });
    }

    private void saveEditTask(TaskItem item, TaskLog log){
        taskDetailViewModel.saveEditTask(item, log, new TaskRepository.SaveTaskCallback() {
            @Override
            public void onSuccess(String successMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(TaskDetailActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(TaskDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}