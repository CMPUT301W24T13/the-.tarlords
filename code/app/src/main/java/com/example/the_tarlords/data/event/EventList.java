package com.example.the_tarlords.data.event;

import static androidx.fragment.app.FragmentManager.TAG;

import static com.example.the_tarlords.MainActivity.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class has an List of events
 * Each List of events could have a name , 2 constructors can have one without a name
 * Handles adding an event to a List
 * These events also have a unique id
 */
public class EventList {
    private ArrayList<Event> events;
    private String name;
    private UUID id;
    private CollectionReference eventsRef;

    public EventList(String name) {
        this.events = new ArrayList<>();
        this.name = name;
        this.id = UUID.randomUUID();
        eventsRef = MainActivity.db.collection("Events");
    }

    public EventList() {
        this.events = new ArrayList<>();
        this.name = "Event Name";
        this.id = UUID.randomUUID();
        eventsRef = MainActivity.db.collection("Events");

    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * Gets list of events associated with a user from firebase
     * @param user
     */
    public static ArrayList<Event> getEventsList(User user) {
        ArrayList<Event> events = new ArrayList<>();
        CollectionReference eventsRef = db.collection("Events");

        eventsRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                MainActivity.db.collection("Events/"+document.getId()+"/Attendance")
                                        .whereEqualTo("user", user.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                                        String id = document.getId();
                                                        String name = document.get("name").toString();
                                                        String location = document.get("location").toString();
                                                        String startTime = document.get("startTime").toString();
                                                        String endTime = document.get("endTime").toString();
                                                        String startDate = document.get("startDate").toString();
                                                        events.add(new Event(name, location, id, startTime, endTime, startDate));
                                                        Log.d("query events", doc.getId() + " => " + doc.getData());
                                                        Log.d("events list", events.toString()+"hi");
                                                        Log.d("query events", document.getId() + " =>=> " + document.getData());
                                                    }
                                                }
                                            }
                                        });
                                Log.d("query events", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("query events", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return events;
    }

    public Event get(int position) {
        return events.get(position);
    }

    //public int size() {return events.size();}

    /**
     * This method adds an event to the event List , as well as Firebase collection "Events"
     * @param event
     */
    public void addEvent(Event event) {
        this.events.add(event);
        /*
        VERY IMPORTANT NEED TO INFORM THE ARRAY ADPATER/RECYCLER VIEW
         */

        //Next part used to add event to Firebase
        // Create a Map with event details

        // Method without firebase
        Map<String,Object> eventMap = new HashMap<>(); //object means the key can be mapped to any type

        eventMap.put("name", event.getName());
        eventMap.put("location", event.getLocation());
        eventMap.put("id", event.getId().toString());
        eventMap.put("startDate", event.getStartDate());
        eventMap.put("startTime", event.getStartTime());
        eventMap.put("endTime", event.getEndTime());





        // Add the event to the Firestore collection
        eventsRef
                .add(eventMap)
                .addOnSuccessListener(documentReference -> { //could add more listeners , mostly just helpful for debugging
                    // Handle success, if needed
                    //Documents id is not the same as event id
                    Log.d("Firestore", "Event added with ID: " + event.getId());
                });

    }

    /**
     * This method removes an event from eventList as well as the Firebase collection "Events"
     * @param event
     */

    public void removeEvent(Event event) {
        this.events.remove(event);
        /*
        VERY IMPORTANT NEED TO INFORM THE ARRAY ADAPTER/RECYCLER VIEW
         */
        //Next part used to remove event from Firebase

        eventsRef
                .whereEqualTo("id", event.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Found a document with matching event ID, delete it
                            eventsRef
                                    .document(document.getId())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // Handle successful deletion,
                                        Log.d("Firestore", "Event with ID " + event.getId() + " deleted successfully");
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle deletion failure
                                        Log.e("Firestore", "Error deleting event");
                                    });
                        }
                    } else {
                        // Handle query failure, didn't get the right document
                        Log.e("Firestore", "Error getting documents with event ID " + event.getId(), task.getException());
                    }
                });

    }}