package com.example.the_tarlords.data.users;

import java.util.ArrayList;

public class AttendeeSignUpList {
    private static ArrayList<Attendee> attendeeSignUpList;

    public ArrayList<Attendee> getAttendeeSignUpList() {
        return attendeeSignUpList;
    }
    public void addAttendee(Attendee attendee) {
        attendeeSignUpList.add(attendee);
    }

    public void removeAttendee(Attendee attendee) {
        attendeeSignUpList.remove(attendee);
    }

    public static int count() {
        return attendeeSignUpList.size();
    }

}

