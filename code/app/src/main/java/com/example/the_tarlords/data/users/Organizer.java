package com.example.the_tarlords.data.users;

import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.QR.QRCode;
import com.example.the_tarlords.data.app.App;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.event.EventPoster;
import com.example.the_tarlords.data.map.Map;

import java.util.List;

public class Organizer extends Attendee implements OrgPerms {
    public Organizer(User user, Profile profile, Event event) {
        super(user, profile, event);
    }

    @Override
    boolean isOrganizer() {
        return true;
    }

    @Override
    public Event createEvent(String name, String location, Integer id) {
        Event event = new Event(name, location, id);
        return event;
    }

    @Override
    public QRCode genQRCodeForCheckIns(FragmentActivity activity, boolean scan, String text, ImageView imageView) {
        QRCode qrCode = new QRCode(activity, scan);
        qrCode.generateQR(text, imageView);
        return qrCode;
    }

    @Override
    public QRCode reuseQRCode(QRCode qrCode) {
        return qrCode;
    }



    // commenting this out, cuz I need iz's attendee list, or I could create a new getAttendanceCheckInList method in event class
    @Override
    public AttendeeCheckInList viewAttendeeCheckIns(Event event) {
        return event.getAttendeeCheckInList();
    }


    //dont need this method for this part yet
    @Override
    public void sendNotifs(AttendeeCheckInList attendeeCheckInList, String notification) {

    }

    //need Khushi's eventlist to add all the uploaded events to that list (easier to make listview of it as well for xml)
    @Override
    public void uploadEventPoster(EventPoster poster) {

    }

    @Override
    public int trackAttendance(int count) {
        return count;
    }

    @Override
    public Alert receiveAlerts(Alert alert) {
        return alert;
    }

    @Override
    public void shareQRCodeImage(QRCode qrcode, App shareToThisApp) {

    }

    @Override
    public QRCode genUniquePromotionQRCode(FragmentActivity activity, boolean scan, String text, ImageView imageView) {
        QRCode qrCode = new QRCode(activity, scan);
        qrCode.generateQR(text, imageView);
        return qrCode;
    }

    //geolocation stuff we are not done yet
    @Override
    public Map viewUserCheckInPlace(Attendee attendee, Event event) {
        return null;
    }

    //how many times an attendee has checked into an event
    @Override
    public int specificAttendeeCount(Attendee attendee, Event event) {
        return 0;
    }



    //need iz's attendeelist
    @Override
    public AttendeeSignUpList viewAttendeeSignUps(Event event) {
        return null;
    }

    //need iz's attendeelist
    @Override
    public void setLimitOnSignUps(int signUpLimit, Event event) {

    }
}
