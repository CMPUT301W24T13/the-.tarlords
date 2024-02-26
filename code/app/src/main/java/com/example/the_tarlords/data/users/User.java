package com.example.the_tarlords.data.users;

import android.app.Notification;

import com.example.the_tarlords.placeholder.Alert;
import com.example.the_tarlords.placeholder.Event;
import com.example.the_tarlords.placeholder.EventList;
import com.example.the_tarlords.placeholder.Profile;

import java.util.ArrayList;

public abstract class User {
    private Integer userId;
    private Profile profile;
    private EventList events;
    private ArrayList<Alert> alerts;

    public User(Integer userId, Profile profile, EventList events, ArrayList<Alert> alerts) {
        this.userId = userId;
        this.profile = profile;
        this.events = events;
        this.alerts = alerts;
    }

    public Integer getId() {
        return userId;
    }
    public void setId(Integer id) {
        this.userId = id;
    }

    public Profile getProfile() {
        return profile;
    }
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public EventList getEvents() {
        return events;
    }
    public void setEvents(EventList events) {
        this.events = events;
    }

    public ArrayList<Alert> getAlerts() {
        return alerts;
    }
    public void setAlerts(ArrayList<Alert> alerts) {
        this.alerts = alerts;
    }

    boolean isAdmin() {
        return false;
    }
}
