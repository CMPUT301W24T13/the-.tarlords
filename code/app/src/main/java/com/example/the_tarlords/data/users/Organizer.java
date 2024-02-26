package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.event.EventList;

import java.util.ArrayList;

public class Organizer extends Attendee implements OrgPerms {
    public Organizer(Integer userId, Profile profile, EventList events, AlertList alerts, Event event) {
        super(userId, profile, events, alerts, event);
    }

    @Override
    boolean isOrganizer() {
        return true;
    }
}
