package com.example.the_tarlords.data.Alert;

import com.example.the_tarlords.data.event.Event;

public class Announcement extends Alert{
    /**
     * Constructor for an announcement class
     * @param title
     * @param message
     * @param event
     */
    public Announcement(String title, String message, Event event) {
        super(title, message, event);
    }

    public void pushAnnouncements(){
        //...
    }
    // not sure what else needs to be in announcements
}
