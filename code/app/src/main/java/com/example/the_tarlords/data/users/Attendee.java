package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;

import java.util.List;

public class Attendee {
    private Event event;
    private User user;
    private Profile profile;

    private Boolean checkInStatus;
    public Attendee(User user, Profile profile, Event event) {
        this.user = user;
        //hey lucy change this vv in android studio...
        this.profile = profile;
        this.event = event;
        this.checkInStatus = false;
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
    public  Profile getProfile(){
        return profile;
    }
    public Boolean getCheckInStatus() { return checkInStatus; }
    public void setCheckInStatus(boolean checkInStatus) { this.checkInStatus = checkInStatus; }

}