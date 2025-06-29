package com.example.todolist.Utils;

import com.example.todolist.model.TaskItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
}
