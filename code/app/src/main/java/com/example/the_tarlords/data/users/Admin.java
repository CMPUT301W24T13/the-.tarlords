package com.example.the_tarlords.data.users;

import com.example.the_tarlords.placeholder.Alert;
import com.example.the_tarlords.placeholder.Event;
import com.example.the_tarlords.placeholder.EventList;
import com.example.the_tarlords.placeholder.Profile;

import java.util.ArrayList;

public class Admin extends User implements AdminPerms {
    public Admin(Integer userId, Profile profile, EventList events, ArrayList<Alert> alerts) {
        super(userId, profile, events, alerts);
    }

    @Override
    boolean isAdmin() {
        return true;
    }
}
