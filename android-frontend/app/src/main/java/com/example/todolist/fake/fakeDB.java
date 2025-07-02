package com.example.todolist.fake;

import com.example.todolist.Utils.DateTimeUtils;
import com.example.todolist.model.Tag;
import com.example.todolist.model.TaskItem;
import com.example.todolist.model.TaskLog;

import java.util.ArrayList;
import java.util.List;

public class fakeDB {
    public static List<Tag> getAllTag(){
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag(1, "#F96060", "Work"));
        tagList.add(new Tag(2, "#6074F9", "Habit"));
        tagList.add(new Tag(3, "#313131", "Market"));
        tagList.add(new Tag(4, "#03DAC5", "Most Important"));
        tagList.add(new Tag(5, "#E0BBE4", "Oh Wow!"));
        return tagList;
    }

//    public static List<TaskItem> getAllTask(){
//        List<TaskItem> list = new ArrayList<>();
//        list.add(new TaskItem(1, "Completed Routine 1", "Time is Money!", "2025-06-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 1));
//        list.add(new TaskItem(2, "Completed Routine 2", "Time is Money!", "2025-06-20", "2025-08-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 2));
//        list.add(new TaskItem(3, "Completed Routine 3", "Time is Money!", "2025-06-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 3));
//        list.add(new TaskItem(4, "Completed Routine 4", "Time is Money!", "2025-07-20", "2025-08-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 4));
//        list.add(new TaskItem(5, "Completed Routine 5", "Time is Money!", "2025-06-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 5));
//        list.add(new TaskItem(6, "Completed Routine 6", "Time is Money!", "2025-07-20", "2025-09-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 4));
//        list.add(new TaskItem(7, "Completed Routine 7", "Time is Money!", "2025-07-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 1));
//        list.add(new TaskItem(8, "Completed Routine 8", "Time is Money!", "2025-07-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 4));
//        list.add(new TaskItem(9, "Completed Routine 9", "Time is Money!", "2025-06-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 3));
//        list.add(new TaskItem(10, "Completed Routine 10", "Time is Money!", "2025-07-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 1));
//        list = DateTimeUtils.filterTaskToday(list);
//        return list;
//    }

    public static List<TaskLog> getAllTaskLog(){
        List<TaskLog> taskLogsToday = new ArrayList<>();
//        taskLogsToday.add(new TaskLog(1, 3, "2025-06-30", ""));
        return taskLogsToday;
    }

//    public static TaskItem getTaskItem(int taskId){
//        List<TaskItem> list = new ArrayList<>();
//        list.add(new TaskItem(1, "Completed Routine 1", "Time is Money!", "2025-06-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 1));
//        list.add(new TaskItem(2, "Completed Routine 2", "Time is Money!", "2025-06-20", "2025-08-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 2));
//        list.add(new TaskItem(3, "Completed Routine 3", "Time is Money!", "2025-06-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 3));
//        list.add(new TaskItem(4, "Completed Routine 4", "Time is Money!", "2025-07-20", "2025-08-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 4));
//        list.add(new TaskItem(5, "Completed Routine 5", "Time is Money!", "2025-06-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 5));
//        list.add(new TaskItem(6, "Completed Routine 6", "Time is Money!", "2025-07-20", "2025-09-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 4));
//        list.add(new TaskItem(7, "Completed Routine 7", "Time is Money!", "2025-07-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 1));
//        list.add(new TaskItem(8, "Completed Routine 8", "Time is Money!", "2025-07-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 4));
//        list.add(new TaskItem(9, "Completed Routine 9", "Time is Money!", "2025-06-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 3));
//        list.add(new TaskItem(10, "Completed Routine 10", "Time is Money!", "2025-07-20", "2025-07-30", "16:00:00", "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg", 1));
//        list = DateTimeUtils.filterTaskToday(list);
//        for(TaskItem item : list){
//            if(item.getId() == taskId){
//                return item;
//            }
//        }
//        return new TaskItem();
//    }
}
