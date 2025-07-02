package com.example.todolist.model;

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
    public Tag(int id, String color, String name) {
        this.id = id;
        this.color = color;
        this.name = name;
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
}
