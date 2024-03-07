package com.example.the_tarlords.data.users;

import android.util.Log;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.QR.QRCode;
import com.example.the_tarlords.data.app.App;
import com.example.the_tarlords.data.attendance.Attendance;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.event.EventPoster;
import com.example.the_tarlords.data.map.Map;

import java.util.ArrayList;

//Notes for myself(Rimsha): Finish the methods once others are done their parts
// ask lucy if she is putting event details page and event poster together or not. If they are separate, then create one more method.
/* ask iz if she is working on attendeeSignUpList(). And for the setLimitOnSignUps() thing, can use the reachedMaxCap() method from event
   in SignUp() method from Attendance to check if we have reached max, there is also maxLimit org can set */

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
    public Organizer(String id, String firstName, String lastName, String phoneNum, String email, Profile profile, Event event) {
        super(id, firstName, lastName, phoneNum, email , event);
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
     * This allows the organizer to create a new Event with the specified params
     * @param name
     * @param location
     * @return a new event
     */
    @Override
    public Event createEvent(String name, String location, String id, String startTime, String endTime, String startDate) {
        Event event = new Event(name, location, id, startTime, endTime, startDate);

        return event;
    }

    /**
     * This allows organizer to generate a QR code linked to attendee check in list for that event.
     * It also connects the generated QRcode to a specific event.
     * @param event
     * @param text
     * @param imageView
     * @return
     */
    @Override
    public QRCode genQRCodeForCheckIns(Event event, String text, ImageView imageView) {
        QRCode qrCode = new QRCode();
        qrCode.generateQR(text, imageView);
        event.setQrCodeCheckIns(qrCode);
        return qrCode;
    }

    /**
     * This allows organizer to reuse a previous QR code
     * @param qrCode
     * @return previous QRCode as a new one now
     */
    @Override
    public QRCode reuseQRCode(QRCode qrCode) {
        return qrCode;
    }


    /**
     * NOT DONE!! This allows the organizer to view the attendee check in list
     * @param event
     * @return attendeeCheckInList
     */
    @Override
    public ArrayList<Attendee> viewAttendeeCheckIns(Event event) {
        ArrayList<Attendee> attendeeCheckInList = event.getAttendanceList();
        return attendeeCheckInList;
    }



    /**
     * NOT DONE!! This allows the organizer to get event poster from event and upload it to the app
     * @param event
     */
    @Override
    public void uploadEventPoster(Event event) {
        EventPoster poster = event.getPoster();

        // need lucy's photo class to "upload" it

    }


    // Not needed for this part.
    @Override
    public void sendNotifs(AttendeeCheckInList attendeeCheckInList, String notification) {
    }



    // need iz's attendance list count method
    @Override
    public int trackAttendance(int count) {
        return count;
    }


    // Not sure if we are leaving it; need Jayden's alert class
    @Override
    public Alert receiveAlerts(Alert alert) {
        return alert;
    }


    // need Grace's finished QR code class for exporting
    @Override
    public void shareQRCodeImage(QRCode qrcode, App shareToThisApp) {

    }

    /**
     * This allows organizer to generate a unique promotion QR code linked to the specific event's details pageg.
     * It also connects the generated QRcode to a specific event
     * @param event
     * @param text
     * @param imageView
     * @return
     */
    @Override
    public QRCode genUniquePromotionQRCode(Event event, String text, ImageView imageView) {
        QRCode qrCode = new QRCode();
        qrCode.generateQR(text, imageView);
        event.setQrCodePromo(qrCode);
        return qrCode;
    }

    // Not needed for this part.
    @Override
    public Map viewUserCheckInPlace(Attendee attendee, Event event) {
        return null;
    }

    // need iz's attendance list method
    @Override
    public int specificAttendeeCount(Attendee attendee, Event event) {
        return 0;
    }

    //need iz's attendance list; This code is incorrect, I will change it
    @Override
    public AttendeeSignUpList viewAttendeeSignUps(Event event) {
        return null;
    }

    /**
     * This allows organizer to set a limit on how many people can signup for an event
     * @param maxSignUps
     * @param event
     */
    @Override
    public void setLimitOnSignUps(int maxSignUps, Event event) {
        event.setMaxSignUps(maxSignUps);
        /* for iz: can use the reachedMaxCap() method from event in SignUp() method from Attendance
            to check if we have reached max, there can be*/

    }
}



/*public class Organizer extends Attendee implements OrgPerms {


    public Organizer(String id, String firstName, String lastName, String phoneNum, String email, Profile profile, Event event) {
        super(id, firstName, lastName, phoneNum, email , event);
    }


    @Override
    boolean isOrganizer() {
        return true;
    }


    @Override
    public Event createEvent(String name, String location, String id, String startTime, String endTime, String startDate) {
        Event event = new Event(name, location, id, startTime, endTime, startDate);
        if (setLimit()) {
            int maxLimit = maxLimitFunction();
            event.setMaxNumOfSignUps(maxLimit);
        }
        return event;
    }



    @Override
    public boolean setLimit() {
        return true;
    }



    @Override
    public int maxLimitFunction() {
        Scanner scanner = new Scanner(System.in);
        int maxLimit = scanner.nextInt();
        return maxLimit;
    }




   public QRCode generateQRCode(Event event, String text, ImageView imageView) {
        QRCode qrCode = new QRCode();
        qrCode.generateQR(text, imageView);
        //DO SOMETHING ABOUT THE IMAGEVIEW
        if (text.charAt(0) == 'C') {     // this is for checkins
            event.setCheckInQR(qrCode);
        } else if (text.charAt(0) == 'E') {   // this is for event details
            event.setEventInfoQR(qrCode);
        }
        return qrCode;
    }

    @Override
    public QRCode reuseQRCode(QRCode qrCode) {
        return qrCode;
    }


    @Override
    public ArrayList<Attendee> viewAttendeeCheckIns(Event event) {

        //this gets ALL the attendees signed up for event (including those checkedIn and not checkedIn)
        ArrayList<Attendee> allAttendees = event.getAttendanceList();

        //new list to return only the attendees whose checkedInStatus is true
        ArrayList<Attendee> attendeeCheckInList = new ArrayList<>();
        for (Attendee attendee : allAttendees) {
            if (attendee.getCheckInStatus()) {
                attendeeCheckInList.add(attendee);
            }
        }
        return attendeeCheckInList;
    }

    // Not needed for this part.
    @Override
    public void sendNotifs(ArrayList<Attendee> attendeeCheckInList, String notification) {
    }


    @Override
    public void uploadEventPoster(Event event) {
        EventPoster poster = event.getPoster();

        // need lucy's photo class to "upload" it

    }


    @Override
    public int trackAttendance(Event event) {
        ArrayList<Attendee> attendeeCheckInList = viewAttendeeCheckIns(event);
        return attendeeCheckInList.size();
    }




    // Not sure if we are leaving it; need Jayden's alert class
    @Override
    public Alert receiveAlerts(Alert alert) {
        return alert;
    }


    // need Grace's finished QR code class for exporting
    @Override
    public void shareQRCodeImage(QRCode qrcode, App shareToThisApp) {

    }


    // Not needed for this part.
    @Override
    public Map viewUserCheckInPlace(Attendee attendee, Event event) {
        return null;
    }


    @Override
    public int specificAttendeeCount(Attendee givenAttendee, Event event) {
        ArrayList<Attendee> attendeeCheckInList = viewAttendeeCheckIns(event);
        int attendeeCheckInCount = 0;
        for (Attendee attendee : attendeeCheckInList) {
            if (attendee.equals(givenAttendee)) {
                attendeeCheckInCount++;
            }
        }
        return attendeeCheckInCount;
    }


    @Override
    public ArrayList<Attendee> viewAttendeeSignUps(Event event) {
        //this gets ALL the attendees signed up for event (including those checkedIn and not checkedIn)
        ArrayList<Attendee> attendeeSignUpList = event.getAttendanceList();
        return attendeeSignUpList;
    }

}*/
