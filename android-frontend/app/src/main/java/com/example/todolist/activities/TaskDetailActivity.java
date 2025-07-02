package com.example.todolist.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolist.R;
import com.example.todolist.databinding.ActivityTaskDetailBinding;
import com.example.todolist.fake.fakeDB;
import com.example.todolist.model.TaskItem;
import com.example.todolist.repository.TaskRepository;
import com.example.todolist.viewModel.TaskDetailViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskDetailActivity extends AppCompatActivity {
    private TaskDetailViewModel taskDetailViewModel;
    private TaskItem item;
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
            }
        };

        taskDetailViewModel.task.observe(this, itemObserver);

        //Retrieve Data
        Bundle bundle = getIntent().getExtras();
        int taskId = bundle.getInt("task_id");
        taskDetailViewModel.loadData(taskId);

        // Xử lý khoảng cách hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button selectDateButton = findViewById(R.id.select_date_button);
        TextView selectedDateTextView = findViewById(R.id.selected_date_text_view);

        // Xử lý sự kiện khi nhấn nút "Chọn ngày"
        selectDateButton.setOnClickListener(v -> {
            // Lấy ngày bắt đầu và ngày kết thúc
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = sdf.parse(item.getStartDate());
                endDate = sdf.parse(item.getEndDate());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (startDate != null && endDate != null) {
                ArrayList<String> dateList = new ArrayList<>();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);

                // Tạo danh sách các ngày
                while (!calendar.getTime().after(endDate)) {
                    dateList.add(sdf.format(calendar.getTime()));
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                // Hiển thị dialog với danh sách ngày
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Chọn ngày");
                builder.setItems(dateList.toArray(new String[0]), (dialog, which) -> {
                    String selectedDate = dateList.get(which);
                    selectedDateTextView.setText(selectedDate);
                });
                builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
                builder.show();
            }
        });

        binding.backButton.setOnClickListener(v -> finish());
    }

    private void observeData(){
        taskDetailViewModel.task.observe(this, taskItem -> {
            this.item = taskItem;
            if (taskItem != null) {
                binding.titleTextView.setText(taskItem.getName());
                binding.startDateTextView.setText(taskItem.getStartDate());
                binding.endDateTextView.setText(taskItem.getEndDate());
                binding.descriptionTextView.setText(taskItem.getDescription());
            }
        });
    }
}