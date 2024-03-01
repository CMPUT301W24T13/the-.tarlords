package com.example.the_tarlords.data.users;

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
    public Event createEvent(Event event) {
        return null;
    }

    @Override
    public QRCode genQRCodeForCheckIns(QRCode qrCode) {
        return null;
    }

    @Override
    public QRCode reuseQRCode(QRCode qrCode) {
        return null;
    }

    @Override
    public AttendeeCheckInList viewAttendeeCheckIns(Event event) {
        return null;
    }

    @Override
    public void sendNotifs(AttendeeCheckInList attendeeCheckInList, String notification) {

    }

    @Override
    public void uploadEventPoster(EventPoster poster) {

    }

    @Override
    public void trackAttendance(int count) {

    }

    @Override
    public Alert receiveAlerts(Alert alert) {
        return null;
    }

    @Override
    public void shareQRCodeImage(QRCode qrcode, App shareToThisApp) {

    }

    @Override
    public QRCode genUniquePromotionQRCode(QRCode qrCode) {
        return null;
    }

    @Override
    public Map viewUserCheckInPlace(Attendee attendee, Event event) {
        return null;
    }

    @Override
    public int specificAttendeeCount(Attendee attendee, Event event) {
        return 0;
    }

    @Override
    public AttendeeSignUpList viewAttendeeSignUps(Event event) {
        return null;
    }

    @Override
    public void setLimitOnSignUps(int signUpLimit, Event event) {

    }
}
