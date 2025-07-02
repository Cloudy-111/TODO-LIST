package com.example.todolist.model;

import androidx.annotation.NonNull;

public class Tag {
    private int id;
    private String color;
    private String name;
    private int userId;

    public Tag(int id, int userId, String color, String name) {
        this.id = id;
        this.userId = userId;
        this.color = color;
        this.name = name;
    }
    public Tag(int userId, String color, String name) {
        this.userId = userId;
        this.color = color;
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name + " " + this.color;
    }
}
