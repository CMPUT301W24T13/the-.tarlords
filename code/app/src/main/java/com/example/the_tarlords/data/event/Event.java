package com.example.the_tarlords.data.event;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.QR.QRCode;
import com.example.the_tarlords.data.attendance.Attendance;
import com.example.the_tarlords.data.users.Attendee;
import com.example.the_tarlords.data.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This class defines an event
 * UUID type for event attribute makes sure that everytime an event object is created it has a unique id
 * Not sure how QRcode will work , is it initialized when the event is created, or can it be set after being created
 */

/*NOTE FOR EVERYONE:
    Firebase integration is a pain. I couldnt do the getter and setter methods normally like
    public String getId() {return id}

    I had to use success and failure listners and pass it in the params of
    public void getId(success listener, failure listener)

    basically "return id" is too fast for firebase, it cant wait for firebase to finish fetching
    the data from firestore so it either returns null or the previous stored value of id.
    I have already created the methods: I just want to show you how to use these methods whenever you want to call
    them.
    Example, you make an event in some other class:
    Event event = new Event("Sample Event", "Sample Location", "sample_id", "start_time", "end_time", "start_date");

    if you wanna get the event id, do thiS:

    event.getId(
                id -> {                                // successlistener; here now you have access to returned id and you can do other things with it
                    Log.d("Event", id);
                    //do other things here with id whatever you wanted the id for
                }
        );

    If you wanna set the event id, do this: and you need all three arguments or it will throw error

    event.setId(
                "idnumber",
                success -> Log.d("Event", "ID successfully set")
                failure -> Log.e("Event", "Error message")
        );

);
);

*/


/* NOTE FOR KHUSHI AND GRACE:

    2. Need to connect event location to the Map Class.
*/
public class Event implements Attendance, Parcelable {
    String name;
    String location;
    String startTime;
    String endTime;
    String startDate;
    String id;
    String organizerId;
    private QRCode qrCodeCheckIns;
    private QRCode qrCodePromo;

    private EventPoster poster;

    private Integer maxSignUps;

    //this is accessing the same FirebaseFirestore from mainactivity's static db
    private FirebaseFirestore db = MainActivity.db;

    private CollectionReference attendanceRef = MainActivity.db.collection("Events/"+ id +"/Attendees");

    private CollectionReference usersRef = MainActivity.db.collection("Users");

    //this is static so that all instances of the Event class have the same eventsRef. So Im not including this ref in the constructors
    private static CollectionReference eventsRef = MainActivity.db.collection("Events");


    public Event(String name, String location, String id, String startTime, String endTime, String startDate) {
        this.name = name;
        this.location = location;
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.attendanceRef = db.collection("Events").document(id).collection("Attendees");
        this.usersRef = db.collection("Users");
    }

    public Event (Parcel in) {
        name = in.readString();
        location = in.readString();
        id = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        startDate = in.readString();
        this.attendanceRef = db.collection("Events").document(id).collection("Attendees");
        this.usersRef = db.collection("Users");

    }

    // NOTE! LEAVE THIS ONE EMPTY, DONT ADD ANYTHING OR IT CRASHES
    public Event (){
    };


    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {

        this.id = id;
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

    public Integer getMaxSignUps() {
        return maxSignUps;
    }

    public void setMaxSignUps(Integer maxSignUps) {
        this.maxSignUps = maxSignUps;
    }

    public boolean reachedMaxCap() {
        return true;
    }


    /**
     * Returns a list of Attendee objects attending the event.
     *
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
                        String id = userDoc.getId();
                        String firstName = userDoc.get("firstName").toString();
                        String lastName = userDoc.get("lastName").toString();
                        String email = userDoc.get("email").toString();
                        String phoneNum = userDoc.get("phoneNum").toString();

                        //User user = userDoc.toObject(User.class);
                        Attendee attendee = new Attendee(id, firstName,lastName,phoneNum,email,Event.this);
                        attendee.setCheckInStatus(attendeeDoc.getBoolean("checkedInStatus"));
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
     * Sets check in status of a user for an event.
     * @param user to check in, status to set (boolean)
     */
    public void setCheckIn(User user, Boolean status) {
        attendanceRef
                .document(user.getId().toString())
                .set(status)
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(id);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(startDate);
    }


}














