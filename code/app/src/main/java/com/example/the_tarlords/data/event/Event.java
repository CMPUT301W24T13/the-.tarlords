package com.example.the_tarlords.data.event;

import static androidx.fragment.app.FragmentManager.TAG;

import static com.example.the_tarlords.MainActivity.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.QR.QRCode;
import com.example.the_tarlords.data.attendance.Attendance;
import com.example.the_tarlords.data.users.Attendee;
import com.example.the_tarlords.data.users.Organizer;
import com.example.the_tarlords.data.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class defines an event
 * UUID type for event attribute makes sure that everytime an event object is created it has a unique id
 * Not sure how QRcode will work , is it initialized when the event is created, or can it be set after being created
 */


/* NOTE FOR KHUSHI AND GRACE:

    2. Need to connect event location to the Map Class.
*/
public class Event implements Attendance {
    String name;
    String location;
    String startTime;
    String endTime;
    String startDate;
    UUID id;
    private QRCode qrCodeCheckIns;
    private QRCode qrCodePromo;

    private EventPoster poster;
    private int maxNumOfSignUps;
    private CollectionReference attendanceRef = MainActivity.db.collection("Events/"+ id +"/Attendees");
    private CollectionReference usersRef = MainActivity.db.collection("Users");


    public Event(String name, String location) {
        this.name = name;
        this.location = location;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setQrCodeCheckIns(QRCode qrCode) {
        this.qrCodeCheckIns = qrCode;
    }

    public void setQrCodePromo(QRCode qrCode) {
        this.qrCodePromo = qrCode;
    }

    public QRCode getQrCodeCheckIns() {
        return qrCodeCheckIns;
    }

    public QRCode getQrCodePromo() {
        return qrCodePromo;
    }

    public EventPoster getPoster() {
        return poster;
    }

    public void setPoster(EventPoster poster) {
        this.poster = poster;
    }

    public int getMaxNumOfSignUps() {
        return maxNumOfSignUps;
    }

    public void setMaxNumOfSignUps(int maxNumOfSignUps) {
        this.maxNumOfSignUps = maxNumOfSignUps;
    }

    /**
     * Returns a list of Attendee objects attending the event. This is the default "signup" list
     * Updates the user's checked in status if they check in or not.
     * @return list of User objects
     */
    public ArrayList<Attendee> getAttendanceList() {
        ArrayList<Attendee> attendees = new ArrayList<Attendee>();
        attendanceRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot attendeeDoc : task.getResult()) {
                        DocumentSnapshot userDoc = usersRef.document(attendeeDoc.getId()).get().getResult();
                        User user = userDoc.toObject(User.class);
                        Attendee attendee = new Attendee(user, user.getProfile(),Event.this);
                        attendee.setCheckInStatus(userDoc.getBoolean("checkedInStatus"));
                        attendees.add(attendee);
                    }
                    Log.d("firestore", attendees.toString());
                } else {
                    Log.d("firestore", "Error getting documents: ", task.getException());
                }
            }
        });
        return attendees;
    }
    /**
     * Signs up a user to attend an event by adding their name to the attendance list.
     *
     * @param user to add
     */
    public void signUp(User user) {
        attendanceRef
                .document(user.getId().toString())
                .set(false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", e.getMessage());
                    }
                });
    }

    /**
     * Removes a user from the attendance list of an event.
     *
     * @param user to remove
     */
    public void removeSignUp(User user) {
        attendanceRef
                .document(user.getId().toString())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", e.getMessage());
                    }
                });
    }

    /**
     * Checks in a user for an event by updating the checked in status for that event.
     * @param user to check in
     */
    public void checkIn(User user) {
        attendanceRef
                .document(user.getId().toString())
                .set(true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", e.getMessage());
                    }
                });
    }

    /**
     * Removes user's checked in status for an event. Possibly unnecessary.
     * @param user to un check in
     */
    public void removeCheckIn(User user) {
        attendanceRef
                .document(user.getId().toString())
                .set(false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", e.getMessage());
                    }
                });
    }




}
