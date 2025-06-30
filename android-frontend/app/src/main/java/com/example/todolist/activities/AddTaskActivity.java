package com.example.todolist.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolist.R;
import com.example.todolist.Utils.DateTimeUtils;
import com.example.todolist.databinding.ActivityAddTaskBinding;
import com.example.todolist.model.TaskItem;
import com.example.todolist.repository.TaskRepository;
import com.example.todolist.viewModel.AddTaskViewModel;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddTaskActivity extends AppCompatActivity {
    private static final OkHttpClient client = new OkHttpClient();
    private ActivityAddTaskBinding binding;
    private ImageView chooseIcon;
    private TextView startDateTextView, endDateTextView, remindHourTextView;
    private EditText editDescriptionText, editTitleText;
    private Button saveButton;
    private AddTaskViewModel addTaskViewModel;

    private int[] iconOptions = {
            R.drawable.ic_laundry,
            R.drawable.ic_calendar,
            R.drawable.ic_arrow_back_black_24dp
    };

    private String[] iconNames = {
            "Giặt đồ", "Lịch", "Quay lại"
    };

    private static final int REQUEST_CODE_PERMISSION = 101;

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        chooseIcon.setImageURI(selectedImageUri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        chooseIcon = findViewById(R.id.choose_icon);
        startDateTextView = binding.startDateTextView;
        endDateTextView = binding.endDateTextView;
        editDescriptionText = binding.descriptionEditTextView;
        editTitleText = binding.titleEditTextView;
        saveButton = binding.saveButton;
        remindHourTextView = binding.remindHour;

        addTaskViewModel = new ViewModelProvider(this).get(AddTaskViewModel.class);

//        chooseIcon.setOnClickListener(v -> showIconDialog());

        startDateTextView.setOnClickListener(v -> DateTimeUtils.showDatePicker(startDateTextView));
        endDateTextView.setOnClickListener(v -> DateTimeUtils.showDatePicker(endDateTextView));


        remindHourTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimeUtils.showTimePicker(remindHourTextView);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskItem newItem = new TaskItem(1, editTitleText.getText().toString(),
                        editDescriptionText.getText().toString(),
                        remindHourTextView.getText().toString(),
                        "",
                        1,
                        startDateTextView.getText().toString(),
                        endDateTextView.getText().toString());
                saveNewTask(newItem);
            }
        });

        binding.backButton.setOnClickListener(v -> finish());
    }

    private void saveNewTask(TaskItem item){
        addTaskViewModel.saveNewTask(item, new TaskRepository.AddTaskCallback() {
            @Override
            public void onSuccess(int userId) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
                    intent.putExtra("user_id", userId);
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(AddTaskActivity.this, errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}