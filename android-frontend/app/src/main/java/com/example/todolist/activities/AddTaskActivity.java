package com.example.todolist.activities;


import static java.security.AccessController.getContext;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.adapter.TagAdapter;
import com.example.todolist.repository.TagRepository;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.example.todolist.R;
import com.example.todolist.Utils.DateTimeUtils;
import com.example.todolist.databinding.ActivityAddTaskBinding;
import com.example.todolist.model.Tag;
import com.example.todolist.model.TaskItem;
import com.example.todolist.repository.TaskRepository;
import com.example.todolist.viewModel.AddTaskViewModel;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private TextView startDateTextView, endDateTextView, remindHourTextView, tagNameDisplay;
    private EditText editDescriptionText, editTitleText, editTagName;
    private Button saveButton, chooseColor, btnCancel, btnConfirm;
    private View tagColorPreview;
    private Tag selectedTag;
    private Calendar startDateCalendar, endDateCalendar;
    private AddTaskViewModel addTaskViewModel;
    private SharedPreferences preferences;
    private List<Tag> tagList = new ArrayList<>();
    private TagAdapter tagAdapter;
    private int userId;
    private int selectedColorValue = Color.parseColor("#000000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = preferences.getInt("user_id", 0);

        startDateTextView = binding.startDateTextView;
        endDateTextView = binding.endDateTextView;
        editDescriptionText = binding.descriptionEditTextView;
        editTitleText = binding.titleEditTextView;
        saveButton = binding.saveButton;
        remindHourTextView = binding.remindHour;

        addTaskViewModel = new ViewModelProvider(this).get(AddTaskViewModel.class);
        observeData();

        tagNameDisplay = findViewById(R.id.tag_name_display);
        tagColorPreview = findViewById(R.id.tag_color_preview);

        startDateTextView.setOnClickListener(v -> DateTimeUtils.showDatePicker(startDateTextView));
        endDateTextView.setOnClickListener(v -> DateTimeUtils.showDatePicker(endDateTextView));

        View tagContainer = findViewById(R.id.edit_tag_name);
        if (tagContainer == null) {
            tagContainer = findViewById(R.id.tag_name_display).getParent() instanceof View ?
                    (View) findViewById(R.id.tag_name_display).getParent() : null;
        }
        if (tagContainer != null) {
            tagContainer.setOnClickListener(v -> showTagSelectorDialog());
        }

        ImageView backButton = binding.backButton;
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddTaskActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        });

        remindHourTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimeUtils.showTimePicker(remindHourTextView);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate inputs
                if (editTitleText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddTaskActivity.this, "Vui lòng nhập tiêu đề công việc", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (startDateTextView == null) {
                    Toast.makeText(AddTaskActivity.this, "Vui lòng chọn ngày bắt đầu", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (endDateTextView == null) {
                    Toast.makeText(AddTaskActivity.this, "Vui lòng chọn ngày kết thúc", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedTag == null) {
                    Toast.makeText(AddTaskActivity.this, "Vui lòng chọn chủ đề", Toast.LENGTH_SHORT).show();
                    return;
                }

                TaskItem newItem = new TaskItem(userId,
                        editTitleText.getText().toString(),
                        editDescriptionText.getText().toString(),
                        remindHourTextView.getText().toString(),
                        "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg",
                        selectedTag.getId(),
                        startDateTextView.getText().toString(),
                        endDateTextView.getText().toString());
                saveNewTask(newItem);
            }
        });
    }

    private void showTagSelectorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_tag_selector, null);
        builder.setView(dialogView);

        btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        tagColorPreview = dialogView.findViewById(R.id.selected_color_preview);
        editTagName = dialogView.findViewById(R.id.edit_tag_name);

        AlertDialog dialog = builder.create();

        // Initialize dialog components
        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_tag_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tagAdapter = new TagAdapter(tagList, tag -> {
            selectedTag = tag;

            tagNameDisplay.setText(tag.getName());
            try {
                int color = Color.parseColor(tag.getColor());
                tagColorPreview.setBackgroundTintList(ColorStateList.valueOf(color));
            } catch (Exception e) {
                tagColorPreview.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            }

            dialog.dismiss();
            Toast.makeText(this, "Đã chọn chủ đề: " + tag.getName(), Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(tagAdapter);
        addTaskViewModel.loadAllTagsByUserId(userId);

        chooseColor = dialogView.findViewById(R.id.btn_pick_custom_color);
        chooseColor.setOnClickListener(v -> pickCustomColor());

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagName = editTagName.getText().toString().trim();
                if (tagName.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Vui lòng nhập tên chủ đề", Toast.LENGTH_SHORT).show();
                    return;
                }

                String hexColor = String.format("#%06X", (0xFFFFFF & selectedColorValue));
                selectedTag = new Tag(userId, hexColor, tagName);

                tagNameDisplay.setText(tagName);
                tagColorPreview.setBackgroundTintList(ColorStateList.valueOf(selectedColorValue));

                saveNewTag(selectedTag);

                dialog.dismiss();
                Toast.makeText(getBaseContext(), "Đã chọn chủ đề: " + tagName, Toast.LENGTH_SHORT).show();

            }
        });
        dialog.show();
    }

    private void observeData(){
        addTaskViewModel.tagList.observe(this, tagList -> {
            this.tagList = tagList;
            tagAdapter.updateData(tagList);
        });
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

    private void saveNewTag(Tag tag){
        addTaskViewModel.saveNewTag(tag, new TagRepository.AddNewTagCallback() {
            @Override
            public void onSuccess(Tag newTag) {
                selectedTag = newTag;
                runOnUiThread(() -> {
                    Toast.makeText(AddTaskActivity.this, "Add Tag Success", Toast.LENGTH_SHORT).show();
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

    private void pickCustomColor(){
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Pick a Color")
                .initialColor(selectedColorValue)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(color -> {

                })
                .setPositiveButton("Chọn", (colorDialog, color, allColors) -> {
                    selectedColorValue = color;
                    if (tagColorPreview != null) {
                        tagColorPreview.setBackgroundTintList(ColorStateList.valueOf(color));
                    }
                    Toast.makeText(this, "Đã chọn màu", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", (colorDialog, which) -> {
                    // Do nothing
                })
                .build()
                .show();
    }
}
