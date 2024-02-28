package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.event.EventList;

import java.util.ArrayList;

public class Attendee extends User {
    private Event event;

    public Attendee(Integer userId, Profile profile, EventList events, AlertList alerts, Event event) {
        super(userId, profile, events, alerts);
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
    }

    boolean isOrganizer() {
        return false;
    }
}
