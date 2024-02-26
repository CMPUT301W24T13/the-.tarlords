package com.example.the_tarlords.data.users;

import com.example.the_tarlords.placeholder.Alert;
import com.example.the_tarlords.placeholder.Event;
import com.example.the_tarlords.placeholder.EventList;
import com.example.the_tarlords.placeholder.Profile;

import java.util.ArrayList;

public class Attendee extends User {
    private Event event;

    public Attendee(Integer userId, Profile profile, EventList events, ArrayList<Alert> alerts, Event event) {
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
