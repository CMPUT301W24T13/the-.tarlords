package com.example.the_tarlords.data.Alert;

import com.example.the_tarlords.data.event.Event;

public class Milestone extends Alert{
    private String title;
    private String message;
    private int count;

    public Milestone(String title, String message, int count) {
        super(title, message,null);
        this.title = title;
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
