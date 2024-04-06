package com.example.the_tarlords.data.event;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class has an List of events
 * Each List of events could have a name , 2 constructors can have one without a name
 * Handles adding an event to a List
 * These events also have a unique id
 */
public class EventListDBHelper {
    private static CollectionReference eventsRef = MainActivity.db.collection("Events");

    /**
     * Gets the list of events that a specific user is attending
     * @param user
     * @param callback
     */
    public static void getEventsAttendingList(User user, EventListCallback callback){
        ArrayList<Event> events = new ArrayList<>();
        eventsRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot eventDoc : task.getResult()) {
                                MainActivity.db.document("Events/"+eventDoc.getId()+"/Attendance/"+user.getUserId())
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()&&task.getResult().exists()){
                                                    events.add(eventDoc.toObject(Event.class));
                                                    Log.d("query events", eventDoc.getId() + " => " + eventDoc.getData());
                                                    callback.onEventListLoaded(events);
                                                }
                                            }
                                        });

                            }
                        } else {
                            Log.d("query events", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Gets the list of events a specfic User has organized
     * @param user
     * @param callback
     */

    public static void getEventsOrganizingList(User user, EventListCallback callback){
        ArrayList<Event> events = new ArrayList<>();
        eventsRef
                .whereEqualTo("organizerId", MainActivity.user.getUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                events.add(document.toObject(Event.class));
                                Log.d("query events", document.getId() + " => " + document.getData());
                                callback.onEventListLoaded(events);
                            }
                        }
                        else {
                            Log.d("query events", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Gets the list of all events in app
     * @param callback
     */

    public static void getEventsList(EventListCallback callback){
        Date d = new Date();
        ArrayList<Event> events = new ArrayList<>();
        eventsRef
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        events.add(document.toObject(Event.class));
                        Log.d("query events", document.getId() + " => " + document.getData());
                        callback.onEventListLoaded(events);
                    }
                } else {
                    Log.d("query events", "Error getting documents: ", task.getException());
                }
            }
        });
    }

}
