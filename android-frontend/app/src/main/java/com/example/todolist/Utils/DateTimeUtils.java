package com.example.todolist.Utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.TextView;

import com.example.todolist.model.TaskItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateTimeUtils {
    public static String getTodayDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public static List<TaskItem> filterTaskToday(List<TaskItem> fullTaskList){
        String todayStr = getTodayDate();
        List<TaskItem> result = new ArrayList<>();

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date today = sdf.parse(todayStr);

            for (TaskItem task : fullTaskList) {
                Date start = sdf.parse(task.getStartDate());
                Date end = sdf.parse(task.getEndDate());

                if ((today.equals(start) || today.after(start)) &&
                        (today.equals(end) || today.before(end))) {
                    result.add(task);
                }
            }
        } catch (ParseException e){
            e.printStackTrace();
        }

        return result;
    }

    public static void showDatePicker(final TextView targetView) {
        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                targetView.getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    targetView.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    public static void showTimePicker(final TextView targetView) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                targetView.getContext(),
                (view, selectedHour, selectedMinute) -> {
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    targetView.setText(time);
                },
                hour, minute, true
        );

        timePickerDialog.show();
    }
}
