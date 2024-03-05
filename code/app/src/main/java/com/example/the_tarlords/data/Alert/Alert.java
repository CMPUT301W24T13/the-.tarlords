package com.example.the_tarlords.data.Alert;

import com.example.the_tarlords.data.event.Event;

import java.io.Serializable;

public class Alert implements Serializable {
    private String title;
    private String message;
    private Event event;

    public Alert(String title, String message, Event event) {
        this.title = title;
        this.message = message;
        this.event = event;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
    }
}
