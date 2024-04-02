package com.example.the_tarlords.data.attendance;

import com.example.the_tarlords.data.users.Attendee;

import java.util.ArrayList;

public interface AttendanceCallback {
    void onAttendanceLoaded(ArrayList<Attendee> attendanceList);
}
