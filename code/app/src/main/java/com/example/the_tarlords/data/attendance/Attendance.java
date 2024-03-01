package com.example.the_tarlords.data.attendance;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.event.EventList;
import com.example.the_tarlords.data.users.Attendee;
import com.example.the_tarlords.data.users.Profile;
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
import java.util.List;

public class Attendance {       //could be made into interface
    private Event event;
    private CollectionReference attendanceRef;
    private CollectionReference usersRef = MainActivity.db.collection("Users");

    public Attendance(Event event) {
        this.event = event;
        attendanceRef = MainActivity.db.collection("Events/"+event.getName()+"/Attendees"); // get name should be getId once implemented in event class
    }

    /**
     * Returns a list of users attending the event.
     *
     * @return list of User objects
     */
    public ArrayList<User> getAttendanceList() {
        ArrayList<User> attendees = new ArrayList<User>();
        attendanceRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot attendeeDoc : task.getResult()) {
                        DocumentSnapshot userDoc = usersRef.document(attendeeDoc.getId()).get().getResult();
                        User user = userDoc.toObject(User.class);
                        // TODO: pass checked in status into User object (requires setCheckedIn method in User class)
                        attendees.add(user);
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
