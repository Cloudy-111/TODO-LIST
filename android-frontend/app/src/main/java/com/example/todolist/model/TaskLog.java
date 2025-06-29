package com.example.todolist.model;

import java.util.Date;

public class TaskLog {
    private int id;
    private int taskId;
    private String completedAt;
    private String note;

    public TaskLog(int id, int taskId, String completedAt, String note) {
        this.id = id;
        this.taskId = taskId;
        this.completedAt = completedAt;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}