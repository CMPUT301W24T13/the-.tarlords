package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.EventList;

import java.util.ArrayList;

public class Admin extends User implements AdminPerms {
    public Admin(Integer userId, Profile profile, EventList events, AlertList alerts) {
        super(userId, profile, events, alerts);
    }

    @Override
    boolean isAdmin() {
        return true;
    }
}
