package com.example.the_tarlords.data.announcement;

import android.os.Build;

import com.example.the_tarlords.data.event.Event;

import java.time.LocalDateTime;

/**
 * This class represents announcements
 */
public class Announcement {
    private String header;
    private String content;
    private Event event;
    private LocalDateTime ldt;

    /**
     * Constructor for the announcement object
     * @param header
     * @param content
     */
    public Announcement(String header, String content, Event event) {
        this.header = header;
        this.content = content;
        this.event = event;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.ldt = LocalDateTime.now();
        }
    }

    /**
     * Getter for the announcement header
     * @return header
     */
    public String getHeader() {
        return header;
    }

    /**
     * Setter for the announcement header
     * @param header
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * getter for the associated event
     * @return event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * setter for the associated event
     * @param event
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * getter for local date time
     * @return ldt
     */
    public LocalDateTime getLdt() {
        return this.ldt;
    }

    /**
     * setter for local date time
     * @param ldt
     */
    public void setLdt(LocalDateTime ldt) {
        this.ldt = ldt;
    }

    /**
     * getter for the announcement content
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * setter for the announcement content
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }
}