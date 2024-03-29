package com.example.the_tarlords.data.users;

import android.widget.ImageView;

import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.QR.QRCode;
import com.example.the_tarlords.data.app.App;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.not_in_use.Map;

import java.util.ArrayList;

public interface OrgPerms {
    public Event createEvent(String name, String location, String id, String startTime, String endTime, String startDate);

    boolean setLimit();

    public int maxLimitFunction();

    public QRCode generateQRCode(Event event, String text, ImageView imageView);

    QRCode reuseQRCode(QRCode qrCode);

    ArrayList<Attendee> viewAttendeeCheckIns(Event event);

    //Note for notifs, make sure in the notifs class there is a part that lets org/admin write their own message
    void sendNotifs(AttendeeCheckInList attendeeCheckInList, String notification);

    void uploadEventPoster(Event event);

    int trackAttendance(Event event);

    Alert receiveAlerts(Alert alert);

    void shareQRCodeImage(QRCode qrcode, App shareToThisApp);

    Map viewUserCheckInPlace(Attendee attendee, Event event);

    int specificAttendeeCount(Attendee attendee, Event event);

    public ArrayList<Attendee> viewAttendeeSignUps(Event event);

    void setLimitOnSignUps(int signUpLimit, Event event);

}






    /*Notes for Lucy :)
    I made these classes: AttendeeList, AttendeeListAdapter (we would eventually need it), EventPoster, QRCode, Map
    I made an App Class because in the project desc under org it says "As an org I want to share a generator QR code image to other apps so I can email or update other documents with the QR code"


    Questions for Lucy:
    Under organizer there is:
    "As an organizer, I want to create a new event and generate a unique QR code for attendee check-ins."
    "As an organizer, I want to create a new event and generate a unique promotion QR code that links to the
    event description and event poster in the app." I don't get it. I am assuming its like whenever org wants to create a new, they should
    have access to those two unique QR codes

    Org wants to see where users are checking in from on the MAP. I am assuming the location means the event location
    and also I am gonna create MAP class and return a pin location that would show on the MAP
    */




