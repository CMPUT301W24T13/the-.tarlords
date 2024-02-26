package com.example.the_tarlords.data.users;

import com.example.the_tarlords.placeholder.Alert;
import com.example.the_tarlords.placeholder.Event;
import com.example.the_tarlords.placeholder.EventList;
import com.example.the_tarlords.placeholder.Profile;

import java.util.ArrayList;

public class Organizer extends Attendee implements OrgPerms {
    public Organizer(Integer userId, Profile profile, EventList events, ArrayList<Alert> alerts, Event event) {
        super(userId, profile, events, alerts, event);
    }

    @Override
    boolean isOrganizer() {
        return true;
    }
}
