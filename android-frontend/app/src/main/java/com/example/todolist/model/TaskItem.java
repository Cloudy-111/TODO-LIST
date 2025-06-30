package com.example.todolist.model;

public class TaskItem {
    private int id;
    private int userId;
    private String name;
    private String description;
    private String due_time;
    private String image_task;
    private int tagId;
    private String startDate;
    private String endDate;

    public TaskItem(int userId, String name, String description, String due_time, String image_task, int tagId, String startDate, String endDate) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.due_time = due_time;
        this.image_task = image_task;
        this.tagId = tagId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TaskItem(int id, String name, String description, String due_time, String image_task) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.due_time = due_time;
        this.image_task = image_task;
    }

    public TaskItem(int id, String name, String description, String startDate, String endDate, String due_time, String image_task, int tagId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.due_time = due_time;
        this.image_task = image_task;
        this.tagId = tagId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public TaskItem() {

    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDue_time() {
        return due_time;
    }

    public void setDue_time(String due_time) {
        this.due_time = due_time;
    }

    public String getImage_task() {
        return image_task;
    }

    public void setImage_task(String image_task) {
        this.image_task = image_task;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}