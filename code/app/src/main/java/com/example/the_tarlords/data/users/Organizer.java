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

/**
 * This is the Organizer Class, which is subclass of Attendee but with more permissions
 */
public class Organizer extends Attendee implements OrgPerms {

    /**
     * This is the constructor for Organizer class
     * @param user
     * @param profile
     * @param event
     */
    public Organizer(User user, Profile profile, Event event) {
        super(user, profile, event);
    }

    /**
     * This checks if the user is an Organizer, because every organizer starts off as an Attendee
     * @return true if it is an organizer, false otherwise
     */
    @Override
    boolean isOrganizer() {
        return true;
    }

    /**
     * This creates a new Event
     * @param name
     * @param location
     * @param id
     * @return
     */
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

    //ask grace
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

    //need lucy's photo class
    @Override
    public void uploadEventPoster(EventPoster poster) {

    }

    //need iz's attendance
    @Override
    public int trackAttendance(int count) {
        return count;
    }


    //leave for later; ask jayden
    @Override
    public Alert receiveAlerts(Alert alert) {
        return alert;
    }


    //grace plsss do this
    @Override
    public void shareQRCodeImage(QRCode qrcode, App shareToThisApp) {

    }

    @Override
    public QRCode genUniquePromotionQRCode(FragmentActivity activity, boolean scan, String text, ImageView imageView) {
        QRCode qrCode = new QRCode(activity, scan);
        qrCode.generateQR(text, imageView);
        return qrCode;
    }

    //geolocation stuff we are not done yet; leave for later
    @Override
    public Map viewUserCheckInPlace(Attendee attendee, Event event) {
        return null;
    }

    //how many times an attendee has checked into an event; need iz's attendance
    @Override
    public int specificAttendeeCount(Attendee attendee, Event event) {
        return 0;
    }

    //need iz's attendeelist
    @Override
    public AttendeeSignUpList viewAttendeeSignUps(Event event) {
        return null;
    }

    //need iz's attendeelist; either iz's part or me
    @Override
    public void setLimitOnSignUps(int signUpLimit, Event event) {

    }
}
