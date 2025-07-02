package com.example.todolist.model;

public class User {
    private int id;
    private String username;
    private String email;
    private String avatar;
    private String password;

    public User(){

    }
    public User(int id, String username, String email, String avatar, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.avatar = avatar;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}