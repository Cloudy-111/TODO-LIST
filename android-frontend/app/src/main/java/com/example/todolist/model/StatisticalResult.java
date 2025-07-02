package com.example.todolist.model;

public class StatisticalResult {
    private int total;
    private int complete;

    public StatisticalResult(int total, int complete) {
        this.total = total;
        this.complete = complete;
    }

    public int getTotal() { return total; }
    public int getComplete() { return complete; }
    public int getRemaining() { return total - complete; }
}
