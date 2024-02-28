package com.example.the_tarlords.data.users;

import java.util.ArrayList;

public class AttendeeCheckInList {
    private static ArrayList<Attendee> attendeeCheckInList;

    public ArrayList<Attendee> getAttendeeCheckInList() {
        return attendeeCheckInList;
    }
    public void addAttendee(Attendee attendee) {
        attendeeCheckInList.add(attendee);
    }

    public void removeAttendee(Attendee attendee) {
        attendeeCheckInList.remove(attendee);
    }

    public static int count() {
        return attendeeCheckInList.size();
    }

}
