package com.example.the_tarlords.data.announcement;

import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.event.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class represents announcements
 */
public class Announcement extends Alert {

    private String currentDateTime;

    /**
     * Constructor for the announcement object
     * @param title string for the title
     * @param message string for the message
     * @param event event that is corresponded
     */
    public Announcement(String title, String message, Event event) {
        super(title, message,event);

        // not sure what else announcement needs
        /*
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.currentDateTime = ldt.format(formatter);]

         */

    }

}
