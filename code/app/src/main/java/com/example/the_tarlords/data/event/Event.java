package com.example.the_tarlords.data.event;

import static androidx.fragment.app.FragmentManager.TAG;

import static com.example.the_tarlords.MainActivity.db;
import static com.google.firebase.firestore.FirebaseFirestore.*;

import android.os.Parcel;
import android.os.Parcelable;
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
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.io.IOException;
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

    private Integer maxNumOfSignUps;

    private CollectionReference attendanceRef = MainActivity.db.collection("Events/"+ id +"/Attendees");
    private CollectionReference usersRef = MainActivity.db.collection("Users");

    private static CollectionReference eventsRef = eventsRef = MainActivity.db.collection("Events");

    public Event(String name, String location, String id, String startTime, String endTime, String startDate) {
        this.name = name;
        this.location = location;
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
    }
    public Event(String name, String location) {
        this.name = name;
        this.location = location;
        makeNewDocID();
    }
    public Event(String name, String location, String id) {
        this.name = name;
        this.location = location;
        this.id = id;
    }
    public Event (Parcel in) {
        name = in.readString();
        location = in.readString();
        id = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        startDate = in.readString();
    }
    public Event (){};

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }
    public String getOrganizerId() {
        return organizerId;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getId() {
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

    public void setMaxSignUps(int maxNumOfSignUps) {
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
                        String id = userDoc.getId();
                        String firstName = userDoc.get("firstName").toString();
                        String lastName = userDoc.get("lastName").toString();
                        String email = userDoc.get("email").toString();
                        String phoneNum = userDoc.get("phoneNum").toString();

                        //User user = userDoc.toObject(User.class);
                        Attendee attendee = null;
                        try {
                            attendee = new Attendee(id, firstName,lastName,phoneNum,email, Event.this);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
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
                .document(user.getUserId().toString())
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
                .document(user.getUserId().toString())
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
                .document(user.getUserId().toString())
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
    //NEED TO JAVADOC
    //Generates a new doc id for the new event, IMPORTANT FOR THE QRCode stuff

    private String newDocID;
    public String makeNewDocID() {
        DocumentReference ref = db.collection("Events").document();
        id = ref.getId();
        return id;
    }
    public void sendToFirebase() {
        // Add the new user document to Firestore
        //MAJOR NOTE THIS AUTOMATICALLY SETS THE DOC ID TO USER ID AND I DONT KNOW IF THAT WOULD BE A PROBLEM
        Map<String, Object> docData = new HashMap<>();
        docData.put("id", id);
        docData.put("name", name);
        docData.put("location", location);
        docData.put("startDate", startDate);
        docData.put("startTime", startTime);
        docData.put("endTime", endTime);
        docData.put("organizerId",organizerId);

        eventsRef.document(id).set(docData)
                .addOnSuccessListener(aVoid -> {
                    // Document successfully added
                    Log.d("debug", "User added successfully to Firestore");
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                    Log.e("debug", "Error adding user to Firestore", e);
                });
    }



}
