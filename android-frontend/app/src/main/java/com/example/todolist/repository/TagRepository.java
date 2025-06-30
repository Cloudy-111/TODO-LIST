package com.example.todolist.repository;

import com.example.todolist.fake.fakeDB;
import com.example.todolist.model.Tag;

import java.util.List;

public class TagRepository {
    public List<Tag> getAllTags() {
        return fakeDB.getAllTag();
    }
}
