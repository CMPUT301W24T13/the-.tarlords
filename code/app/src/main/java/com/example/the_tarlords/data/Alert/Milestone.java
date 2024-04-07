package com.example.the_tarlords.data.Alert;

import com.example.the_tarlords.data.event.Event;

/**
 * Special type of Alert sent out to the Organizers
 * @see Alert
 */

public class Milestone extends Alert{
    private String title;
    private String message;
    private String count;

    public Milestone(String title, String message, String count) {
        super(title, message,null);
        this.title = title;
        this.message = message;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
