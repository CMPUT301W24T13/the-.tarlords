package com.example.the_tarlords.data.attendance;

import android.app.FragmentManager;

import com.example.the_tarlords.data.users.Attendee;
import com.example.the_tarlords.data.users.User;

import java.util.ArrayList;

public interface Attendance {       //could be made into interface

    /**
     * Populates a list of Attendee objects attending the event.
     *
     * @param attendees array list of Attendee objects to populate
     */
    public void populateAttendanceList(ArrayList<Attendee> attendees);

    /**
     * Signs up a user to attend an event by adding their name to the attendance list.
     *
     * @param user to add
     */
    public void signUp(User user);

    /**
     * Removes a user from the attendance list of an event.
     *
     * @param user to remove
     */
    public void removeSignUp(User user);

    /**
     * Sets check in status of a user to bool status for an event.
     *
     * @param user to check in
     */
    public void setCheckIn(User user, Boolean status);

}