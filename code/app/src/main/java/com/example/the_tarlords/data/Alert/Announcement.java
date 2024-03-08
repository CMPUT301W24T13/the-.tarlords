package com.example.the_tarlords.data.Alert;

import com.example.the_tarlords.data.event.Event;

public class Announcement extends Alert{
    /**
     * Constructor for an announcement class
     * @param title
     * @param message
     */
    public Announcement(String title, String message, String currentDateTime) {
        super(title, message,null);
    }

    public void pushAnnouncements(){
        //...
    }
    // not sure what else needs to be in announcements
}
