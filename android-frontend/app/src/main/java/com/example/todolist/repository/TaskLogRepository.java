package com.example.todolist.repository;

import com.example.todolist.fake.fakeDB;
import com.example.todolist.model.TaskLog;

import java.util.List;

public class TaskLogRepository {
    public List<TaskLog> getAllTaskLogs() {
        return fakeDB.getAllTaskLog();
    }
}
