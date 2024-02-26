package com.example.the_tarlords.data.Alert;

import com.example.the_tarlords.placeholder.Event;

public class Alert {
    private String message;
    private Event event;

    public Alert(String message, Event event) {
        this.message = message;
        this.event = event;
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
