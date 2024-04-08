package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.event.Event;

import java.io.IOException;

/**
 * Extends User class, has special attendee permissions
 * Have check-in status and events
 */

public class Attendee extends User{
    private Event event;
    private Integer checkInCount;
    private boolean checkInStatus;
    public Attendee(String userId, String firstName, String lastName, String phoneNum, String email, Event event) throws IOException {
        super(userId, firstName, lastName, phoneNum, email);
        this.event = event;
        this.checkInStatus = false;
    }

    public Attendee(){}

    public Integer getCheckInCount(){return checkInCount;}
    public void setCheckInCount(Integer checkInCount){
        this.checkInCount = checkInCount;
    }
    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
    }

    public Boolean getCheckInStatus() { return checkInStatus; }
    public void setCheckInStatus(boolean checkInStatus) { this.checkInStatus = checkInStatus; }

}