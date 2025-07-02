package com.example.todolist.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private TextView startDateTextView, endDateTextView, remindHourTextView, buttonChooseTag;
    private EditText editDescriptionText, editTitleText;
    private Button saveButton;
    private AddTaskViewModel addTaskViewModel;
    private SharedPreferences preferences;

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

        preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", 0);

//        chooseIcon = findViewById(R.id.choose_icon);
        startDateTextView = binding.startDateTextView;
        endDateTextView = binding.endDateTextView;
        editDescriptionText = binding.descriptionEditTextView;
        editTitleText = binding.titleEditTextView;
        saveButton = binding.saveButton;
        remindHourTextView = binding.remindHour;
//        buttonChooseTag = binding.buttonChooseTag;

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
                TaskItem newItem = new TaskItem(userId, editTitleText.getText().toString(),
                        editDescriptionText.getText().toString(),
                        remindHourTextView.getText().toString(),
                        "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg",
                        2,
                        startDateTextView.getText().toString(),
                        endDateTextView.getText().toString());
                saveNewTask(newItem);
            }
        });
//        buttonChooseTag.setOnClickListener(v -> popUpChooseTag());
        binding.backButton.setOnClickListener(v -> finish());
    }

    private void saveNewTask(TaskItem item){
        addTaskViewModel.saveNewTask(item, new TaskRepository.AddTaskCallback() {
            @Override
            public void onSuccess(String successMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(AddTaskActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
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

//    private void popUpChooseTag(){
//        LayoutInflater inflater = (LayoutInflater) AddTaskActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        @SuppressLint("ResourceType") View layout = inflater.inflate(R.id.popup_choose_a_tag, null);
//        float density = AddTaskActivity.this.getResources().getDisplayMetrics().density;
//        final PopupWindow pw = getPopupWindow(layout, (int) density);
//        // display the pop-up in the center
//        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
//    }
//
//    @NonNull
//    private static PopupWindow getPopupWindow(View layout, int density) {
//        final PopupWindow pw = new PopupWindow(layout, density * 240, density * 285, true);
//        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        pw.setTouchInterceptor(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                    pw.dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
//        pw.setOutsideTouchable(true);
//        return pw;
//    }
}