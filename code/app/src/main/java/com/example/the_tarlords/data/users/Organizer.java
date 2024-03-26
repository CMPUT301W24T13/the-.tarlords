package com.example.the_tarlords.data.users;

import android.widget.ImageView;

import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.QR.QRCode;
import com.example.the_tarlords.data.app.App;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.event.EventPoster;
import com.example.the_tarlords.not_in_use.Map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//Notes for myself(Rimsha): Finish the methods once others are done their parts
// ask lucy if she is putting event details page and event poster together or not. If they are separate, then create one more method.
/* ask iz if she is working on attendeeSignUpList(). And for the setLimitOnSignUps() thing, can use the reachedMaxCap() method from event
   in SignUp() method from Attendance to check if we have reached max, there is also maxLimit org can set */

/**
 * This is the Organizer Class, which is subclass of Attendee but with more permissions
 */

public class Organizer extends Attendee implements OrgPerms {


    /**
     * Constructor for organizer
     * @param id
     * @param firstName
     * @param lastName
     * @param phoneNum
     * @param email
     * @param profile
     * @param event
     */
    public Organizer(String id, String firstName, String lastName, String phoneNum, String email, Profile profile, Event event) throws IOException {
        super();
    }

    /**
     * This checks if the user is an Organizer, because every organizer starts off as an Attendee
     * @return true if it is an organizer, false otherwise
     */
    //@Override
    boolean isOrganizer() {
        return true;
    }

    /**
     * This allows the organizer to create a new Event with the specified params
     * @param name
     * @param location
     * @param id
     * @param startTime
     * @param endTime
     * @param startDate
     * @return
     */
    @Override
    public Event createEvent(String name, String location, String id, String startTime, String endTime, String startDate) {
        Event event = new Event(name, location, id, startTime, endTime, startDate);
        if (setLimit()) {
            int maxLimit = maxLimitFunction();
            event.setMaxSignUps(maxLimit);
        }
        return event;
    }



}
