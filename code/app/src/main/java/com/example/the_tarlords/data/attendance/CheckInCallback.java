package com.example.the_tarlords.data.attendance;

import com.example.the_tarlords.data.users.Attendee;

public interface CheckInCallback {
    void onCheckInComplete(Attendee attendee);
}
