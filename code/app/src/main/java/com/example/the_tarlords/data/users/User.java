package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;

import java.util.List;

public class User {
    private Integer userId;
    private Profile profile;
    private List<Event> events;
    private AlertList alerts;

    public User(Integer userId, Profile profile, List<Event> events, AlertList alerts) {
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
    /*
    public void editProfile() {
        //to be implemented
    }
    */

    public List<Event> getEvents() {
        return events;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public AlertList getAlerts() {
        return alerts;
    }
    public void setAlerts(AlertList alerts) {
        this.alerts = alerts;
    }

    boolean isAdmin() {
        return false;
    }
}
