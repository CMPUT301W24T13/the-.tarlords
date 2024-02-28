package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.event.EventList;

import java.util.ArrayList;

public class Organizer extends Attendee implements OrgPerms {
    public Organizer(User user, Profile profile, Event event) {
        super(user, profile, event);
    }

    @Override
    boolean isOrganizer() {
        return true;
    }
}
