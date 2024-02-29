package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;

import java.util.List;

public class Attendee extends User {
    private Event event;

    public Attendee(Integer userId, Profile profile, List<Event> events, AlertList alerts, Event event) {
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