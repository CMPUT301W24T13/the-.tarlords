package com.example.the_tarlords.data.Alert;

import com.example.the_tarlords.data.event.Event;

public class Milestone extends Alert{
    /**
     * constructor for milestone class
     * @param title
     * @param message
     * @param event
     */
    public Milestone(String title, String message, Event event) {
        super(title, message, event);
    }
}
