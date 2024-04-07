package com.example.the_tarlords.data.attendance;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.users.Attendee;
import com.example.the_tarlords.data.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the check-ins and signups
 * and keeps track of the number of checkins and signups per event
 * and per user
 */
public class AttendanceDBHelper {
    private static CollectionReference eventsRef = MainActivity.db.collection("Events");
    private static CollectionReference usersRef = MainActivity.db.collection("Users");
    public static int FALSE = 0;
    public static int TRUE = 1;
    public static int EVENT_FULL = 2;
    public static int ALREADY_SIGNED_UP = 3;
    public static int ALREADY_CHECKED_IN = 3;
    public static int SUCCESSFUL = 4;
    public static int UNSUCCESSFUL = 5;

    /**
     * Returns an array list with Attendee objects attending the event through the AttendanceListCallback using firestore data.
     * This is the default "signup" list.
     *
     * @param event
     * @param callback attendance callback
     */
    public static void getAttendanceList(Event event, AttendanceListCallback callback){
        CollectionReference attendanceRef = MainActivity.db.collection("Events/"+ event.getId() +"/Attendance");
        ArrayList<Attendee> attendees = new ArrayList<>();
        attendanceRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot attendeeDoc : task.getResult()) {
                        usersRef.document(attendeeDoc.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot userDoc = task.getResult();
                                    Attendee attendee = userDoc.toObject(Attendee.class);
                                    attendee.setCheckInStatus(attendeeDoc.getBoolean("checkedInStatus"));
                                    attendee.setCheckInCount(Integer.valueOf(attendeeDoc.get("checkInCount").toString()));

                                    attendees.add(attendee);
                                    Log.d("attendance query", attendees.toString());
                                    callback.onAttendanceLoaded(attendees);
                                }
                            }
                        });
                    }
                } else {
                    Log.d("firestore", "Error getting documents: ", task.getException());
                }
            }
        });

    }

    /**
     * checks if a user is signed up for an event. Returns a result code via a callback.
     * @param event
     * @param user
     * @param callback
     */
    public static void isSignedUp(Event event, User user, AttendanceQueryCallback callback) {
        DocumentReference attendeeRef = MainActivity.db.document("Events/"+event.getId()+"/Attendance/"+user.getUserId());
        attendeeRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        callback.onQueryComplete(TRUE);
                    } else {
                        callback.onQueryComplete(FALSE);
                    }
                } else {
                    Log.d("Attendance", "Sign up query failed with: ", task.getException());
                }
            }
        });
    }

    /**
     * Checks if a user is checked in for an event. Returns a result code via a callback.
     * @param event
     * @param user
     * @param callback
     */
    public static void isCheckedIn(Event event, User user, AttendanceQueryCallback callback) {
        DocumentReference attendeeRef = MainActivity.db.document("Events/"+event.getId()+"/Attendance/"+user.getUserId());
        attendeeRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if ((boolean) document.get("checkedInStatus")){
                        callback.onQueryComplete(TRUE);
                    } else {
                        callback.onQueryComplete(FALSE);
                    }
                } else {
                    Log.d("Attendance", "Check in query failed with: ", task.getException());
                }
            }
        });
    }

    /**
     * Signs up a user to attend an event by adding their name to the attendance list. Returns result
     * code via a callback.
     *
     * @param event
     * @param user to add
     * @param callback
     */
    public static void signUp(Event event, User user, AttendanceQueryCallback callback) {
        isSignedUp(event, user, new AttendanceQueryCallback() {
            @Override
            public void onQueryComplete(int result) {
                if (result==TRUE){
                    callback.onQueryComplete(ALREADY_SIGNED_UP);
                } else if (event.reachedMaxCap()){
                    callback.onQueryComplete(EVENT_FULL);
                } else {
                    CollectionReference attendanceRef = MainActivity.db.collection("Events/" + event.getId() + "/Attendance");
                    Map<String, Object> docData = new HashMap<>();
                    docData.put("user", user.getUserId());
                    docData.put("event", event.getId());
                    docData.put("checkedInStatus", false);
                    docData.put("checkIns",0);
                    attendanceRef
                            .document(user.getUserId())
                            .set(docData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                                        event.setSignUps(event.getSignUps()+1);
                                        eventsRef.document(event.getId()).update("signUps", event.getSignUps());
                                        callback.onQueryComplete(SUCCESSFUL);
                                    }
                                    else {
                                        Log.d("Firestore", "DocumentSnapshot could not be written");
                                        callback.onQueryComplete(UNSUCCESSFUL);
                                    }
                                }
                            });
                }

            }
        });

    }

    /**
     * Removes a user from the attendance list of an event and updates sign ups and returns the result via callback.
     *
     * @param event to update attendance for
     * @param user to remove
     * @param callback
     */
    public static void removeSignUp(Event event, User user, AttendanceQueryCallback callback) {
        CollectionReference attendanceRef = MainActivity.db.collection("Events/"+ event.getId() +"/Attendance");
        isSignedUp(event, user, new AttendanceQueryCallback() {
            @Override
            public void onQueryComplete(int result) {
                if (result==TRUE){
                    attendanceRef
                            .document(user.getUserId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    event.signUps-=1;
                                    eventsRef.document(event.getId()).update("signUps",event.getSignUps());
                                    Log.d("Firestore", "DocumentSnapshot successfully written!");
                                    callback.onQueryComplete(SUCCESSFUL);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Firestore", e.getMessage());
                                    callback.onQueryComplete(UNSUCCESSFUL);
                                }
                            });
                }
            }
        });

    }

    /**
     * Sets check in status of a user for an event, the user is not already signed up for that event,
     * the method will attempt to first sign them up, then check them in. Returns a result code via a callback.
     *
     * @param event
     * @param user   to check in, status to set (boolean)
     * @param status boolean check-in status to set
     * @param callback
     */
    public static void setCheckIn(Event event, User user, Boolean status, AttendanceQueryCallback callback) {
        DocumentReference attendeeRef = MainActivity.db.document("Events/"+ event.getId() +"/Attendance/"+user.getUserId());
        long increment;
        if (status){increment=1;}
        else{increment= -1;}
        isSignedUp(event, user, new AttendanceQueryCallback() {
            @Override
            public void onQueryComplete(int result) {
                if (result==TRUE){
                    isCheckedIn(event, user, new AttendanceQueryCallback() {
                        @Override
                        public void onQueryComplete(int result) {
                            if (result==TRUE) {
                                attendeeRef.update("checkIns",FieldValue.increment(1));
                                callback.onQueryComplete(ALREADY_CHECKED_IN);
                            }
                            else {
                                Log.d("Attendance", "check in increment");
                                attendeeRef
                                        .update("checkedInStatus", status,"checkIns", FieldValue.increment(increment))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("Attendance", "Check in successful!");
                                                    event.setCheckIns(event.getCheckIns()+1);
                                                    eventsRef.document(event.getId()).update("checkIns", event.getCheckIns());
                                                    callback.onQueryComplete(SUCCESSFUL);
                                                }
                                                else {
                                                    Log.d("Attendance", "Check in failed");
                                                    callback.onQueryComplete(UNSUCCESSFUL);
                                                }
                                            }
                                        });
                            }
                        }
                    });
                } else {
                    signUp(event, user, new AttendanceQueryCallback() {
                        @Override
                        public void onQueryComplete(int result) {
                            if (result == SUCCESSFUL){
                                setCheckIn(event, user, status, callback);
                            } else {
                                callback.onQueryComplete(result);
                            }
                        }
                    });
                }
            }
        });

    }

    /**
     * returns number of times an attendee has check in for an event via a callback.
     * @param event
     * @param user
     * @param callback
     */
    public static void getAttendeeCheckIns(Event event, User user, AttendanceQueryCallback callback){
        DocumentReference attendeeRef = MainActivity.db.document("Events/"+event.getId()+"/Attendance/"+user.getUserId());
        attendeeRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    callback.onQueryComplete((int)task.getResult().get("checkIns"));
                }
            }
        });
    }
    /**
     * returns number of attendees signed up for an event via a callback. Use event.getSignUps() instead if possible.
     * @param event
     * @param callback
     */
    public static void getSignUpCount(Event event, AttendanceQueryCallback callback){
        getAttendanceList(event, new AttendanceListCallback() {
            @Override
            public void onAttendanceLoaded(ArrayList<Attendee> attendanceList) {
                callback.onQueryComplete((int)attendanceList.stream().count());
            }
        });
    }

    /**
     * returns number of attendees checked into an event via a callback. Use event.getCheckIns() instead if possible.
     * @param event
     * @param callback
     */
    public static void getCheckInCount(Event event, AttendanceQueryCallback callback){
        getAttendanceList(event, new AttendanceListCallback() {
            @Override
            public void onAttendanceLoaded(ArrayList<Attendee> attendanceList) {
                int checkInCount=0;
                for (Attendee attendee:attendanceList){
                    if (attendee.getCheckInStatus()){
                        checkInCount+=1;
                    }
                }
                callback.onQueryComplete(checkInCount);
            }
        });
    }

}