package com.example.the_tarlords.data.users;

import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.QR.QRCode;
import com.example.the_tarlords.data.app.App;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.event.EventPoster;
import com.example.the_tarlords.data.map.Map;

import java.util.ArrayList;

public interface OrgPerms {
    Event createEvent(String name, String location);

    public boolean setLimit();

    public int maxLimitFunction();

    QRCode genQRCodeForCheckIns(Event event, String text, ImageView imageView);

    QRCode reuseQRCode(QRCode qrCode);

    ArrayList<Attendee> viewAttendeeCheckIns(Event event);

    //Note for notifs, make sure in the notifs class there is a part that lets org/admin write their own message
    void sendNotifs(ArrayList<Attendee> attendeeCheckInList, String notification);

    void uploadEventPoster(Event event);


    int trackAttendance(Event event);

    Alert receiveAlerts(Alert alert);

    void shareQRCodeImage(QRCode qrcode, App shareToThisApp);


    Map viewUserCheckInPlace(Attendee attendee, Event event);


    int specificAttendeeCount(Attendee givenAttendee, Event event);

    ArrayList<Attendee> viewAttendeeSignUps(Event event);




}







