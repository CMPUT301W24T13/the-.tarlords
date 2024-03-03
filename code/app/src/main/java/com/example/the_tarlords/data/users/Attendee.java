package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;

import java.util.List;

public class Attendee {
    private Event event;
    private User user;
    private Profile profile;
    public Attendee(User user, Profile profile, Event event) {
        this.user = user;
        //hey lucy change this vv in android studio...
        this.profile = profile;
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