package com.example.todolist.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
    private ActivityTaskDetailBinding binding;
    private TaskDetailViewModel taskDetailViewModel;
    private TaskItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        taskDetailViewModel = new ViewModelProvider(this).get(TaskDetailViewModel.class);

        observeData();

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
//package com.example.todolist;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.example.todolist.model.Task;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class TaskDetail extends AppCompatActivity {
//
//    private TextView titleTextView, startDateTextView, endDateTextView, statusTextView, noteTextView;
//    private ImageView backButton;
//    private Button deleteButton, editButton;
//    private OkHttpClient client;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_task_detail);
//
//        // Ánh xạ các view
//        titleTextView = findViewById(R.id.title_text_view);
//        startDateTextView = findViewById(R.id.start_date_text_view);
//        endDateTextView = findViewById(R.id.end_date_text_view);
//        statusTextView = findViewById(R.id.status_text_view);
//        noteTextView = findViewById(R.id.note_text_view);
//        backButton = findViewById(R.id.back_button);
//        deleteButton = findViewById(R.id.delete_button);
//        editButton = findViewById(R.id.edit_button);
//
//        // Khởi tạo OkHttpClient
//        client = new OkHttpClient();
//
//        // Lấy taskId từ Intent
//        int taskId = getIntent().getIntExtra("TASK_ID", -1);
//
//        // Gọi API để lấy dữ liệu
//        fetchTaskDetails(taskId);
//
//        // Xử lý sự kiện nút Back
//        backButton.setOnClickListener(v -> finish());
//
//        // Xử lý sự kiện nút Xóa và Chỉnh sửa
//        deleteButton.setOnClickListener(v -> {
//            Toast.makeText(TaskDetail.this, "Xóa công việc", Toast.LENGTH_SHORT).show();
//        });
//
//        editButton.setOnClickListener(v -> {
//            Toast.makeText(TaskDetail.this, "Chỉnh sửa công việc", Toast.LENGTH_SHORT).show();
//        });
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//
//    private void fetchTaskDetails(int taskId) {
//        String url = "YOUR_API_BASE_URL/tasks/" + taskId; // Thay bằng URL API của bạn
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(() -> Toast.makeText(TaskDetail.this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    try {
//                        String responseBody = response.body().string();
//                        JSONObject json = new JSONObject(responseBody);
//                        Task task = new Task();
//                        task.setId(json.getInt("id"));
//                        task.setTitle(json.getString("title"));
//                        task.setStartDate(json.getString("startDate"));
//                        task.setEndDate(json.getString("endDate"));
//                        task.setStatus(json.getString("status"));
//                        task.setNote(json.getString("note"));
//
//                        runOnUiThread(() -> {
//                            titleTextView.setText(task.getTitle());
//                            startDateTextView.setText(task.getStartDate());
//                            endDateTextView.setText(task.getEndDate());
//                            statusTextView.setText(task.getStatus());
//                            noteTextView.setText(task.getNote());
//                        });
//                    } catch (JSONException e) {
//                        runOnUiThread(() -> Toast.makeText(TaskDetail.this, "Lỗi phân tích dữ liệu", Toast.LENGTH_SHORT).show());
//                    }
//                } else {
//                    runOnUiThread(() -> Toast.makeText(TaskDetail.this, "Không thể lấy dữ liệu", Toast.LENGTH_SHORT).show());
//                }
//            }
//        });
//    }
//}

//import android.app.AlertDialog;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//
//public class TaskDetail extends AppCompatActivity {
//
//    private OkHttpClient client = new OkHttpClient();
//    private TextView titleTextView, startDateTextView, endDateTextView, statusTextView, descriptionTextView, selectedDateTextView;
//    private Button selectDateButton;
//    private EditText taskNoteText; // Thêm EditText nếu cần
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_task_detail);
//
//        // Xử lý khoảng cách hệ thống
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        // Liên kết các view
//        titleTextView = findViewById(R.id.title_text_view);
//        startDateTextView = findViewById(R.id.start_date_text_view);
//        endDateTextView = findViewById(R.id.end_date_text_view);
//        statusTextView = findViewById(R.id.status_text_view);
//        descriptionTextView = findViewById(R.id.description_text_view);
//        selectDateButton = findViewById(R.id.select_date_button);
//        selectedDateTextView = findViewById(R.id.selected_date_text_view);
//        // taskNoteText = findViewById(R.id.task_note_text); // Bổ sung nếu cần
//
//        // Lấy ID task từ Intent (giả sử truyền qua Intent)
//        int taskId = getIntent().getIntExtra("taskId", 1); // Mặc định là 1 nếu không có
//        fetchTaskDetails(taskId);
//
//        // Xử lý sự kiện khi nhấn nút "Chọn ngày"
//        selectDateButton.setOnClickListener(v -> {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//            Date startDate = null;
//            Date endDate = null;
//            try {
//                startDate = sdf.parse(startDateTextView.getText().toString());
//                endDate = sdf.parse(endDateTextView.getText().toString());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if (startDate != null && endDate != null) {
//                ArrayList<String> dateList = new ArrayList<>();
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(startDate);
//
//                // Tạo danh sách các ngày
//                while (!calendar.getTime().after(endDate)) {
//                    dateList.add(sdf.format(calendar.getTime()));
//                    calendar.add(Calendar.DAY_OF_YEAR, 1);
//                }
//
//                // Hiển thị dialog với danh sách ngày
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Chọn ngày");
//                builder.setItems(dateList.toArray(new String[0]), (dialog, which) -> {
//                    String selectedDate = dateList.get(which);
//                    selectedDateTextView.setText(selectedDate);
//                });
//                builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
//                builder.show();
//            }
//        });
//    }
//
//    private void fetchTaskDetails(int taskId) {
//        String url = "http://localhost:8080/api/task/" + taskId; // Thay localhost bằng IP thực tế nếu cần
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                runOnUiThread(() -> Toast.makeText(TaskDetail.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String responseData = response.body().string();
//                    // Giả sử API trả về JSON, bạn cần parse (ví dụ: dùng Gson)
//                    // Dưới đây là ví dụ giả lập parse JSON thủ công
//                    runOnUiThread(() -> {
//                        try {
//                            // Giả lập dữ liệu từ API (thay bằng logic parse JSON thực tế)
//                            String jsonResponse = responseData; // Ví dụ: {"title":"Giặt đồ","startDate":"25/06/2025","endDate":"27/06/2025","status":"Chưa hoàn thành","description":"Công việc cần hoàn thành đúng hạn"}
//                            // Parse thủ công (thay bằng Gson hoặc thư viện khác)
//                            titleTextView.setText("Giặt đồ"); // Thay bằng giá trị từ JSON
//                            startDateTextView.setText("25/06/2025");
//                            endDateTextView.setText("27/06/2025");
//                            statusTextView.setText("Chưa hoàn thành");
//                            descriptionTextView.setText("Công việc cần hoàn thành đúng hạn");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Toast.makeText(TaskDetail.this, "Lỗi phân tích dữ liệu", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });
//    }
//}
// TaskDetail