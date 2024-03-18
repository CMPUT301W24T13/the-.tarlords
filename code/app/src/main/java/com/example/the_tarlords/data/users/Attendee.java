package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;

import java.io.IOException;
import java.util.List;

public class Attendee extends User{
    private Event event;
    //private User user;
    //private Profile profile;

    private Boolean checkInStatus;
   /* public Attendee(User user, Event event) {  // public Attendee(User user, Profile profile, Event event)
        this.user = user;
        //hey lucy change this vv in android studio...
        //this.profile = profile;
        this.event = event;
        this.checkInStatus = false;
    }*/

    public Attendee(String userId, String firstName, String lastName, String phoneNum, String email, Event event) throws IOException {
        super(userId, firstName, lastName, phoneNum, email);
        this.event = event;
        this.checkInStatus = false;
    }

    public Attendee(){}

    @Override
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
    }

    @Override
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    public void setLastName(String lastName) {
        super.setLastName(lastName);
    }

    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
    }
  /*  public User getUser() {return user;}

    public void setUser(User user) {
        this.user = user;
    }*/

    boolean isOrganizer() {
        return false;
    }
    /*public  Profile getProfile(){
        return profile;
    }*/
    public Boolean getCheckInStatus() { return checkInStatus; }
    public void setCheckInStatus(boolean checkInStatus) { this.checkInStatus = checkInStatus; }

}