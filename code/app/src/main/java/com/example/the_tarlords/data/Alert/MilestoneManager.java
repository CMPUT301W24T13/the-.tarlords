package com.example.the_tarlords.data.Alert;

import static java.lang.Math.floor;
import static java.lang.Math.max;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.Notification.Notification;
import com.example.the_tarlords.data.event.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MilestoneManager {

    private int currentCount;
    private int maxCount;
    private static Event event;
    private Notification notification = new Notification();

    /**
     * constructor for the milestone manager class
     * @param event
     */
    public MilestoneManager(Event event){
        this.event = event;
        this.maxCount = event.getMaxSignUps();
        this.currentCount = event.getCheckIns();

    }

    /**
     * checks if a milestone checkpoint is passed. If a checkpoint is passed, create a new milestone and
     * sends a notification to the organizer
     */
    public void updateMilestone() {

        if (currentCount == 1) {
            setMilestone(new Milestone("First milestone", "First attendee checked in", String.valueOf(currentCount)));
            sendMilestoneNotification();

        }if (maxCount > 0 && currentCount == Math.floorDiv(maxCount,2)) {
            setMilestone(new Milestone("Half capacity milestone", "Half of the attendees," + String.valueOf(currentCount) + " of " + String.valueOf(maxCount) + ", are now checked in", String.valueOf(currentCount)));
            sendMilestoneNotification();
        }else if (maxCount > 0 && currentCount == maxCount) {
            setMilestone(new Milestone("Full capacity milestone", "All " + String.valueOf(currentCount) + " attendees now are checked in", String.valueOf(currentCount)));
            sendMilestoneNotification();
        }else if (currentCount > 0 && currentCount % 5 == 0) {
            setMilestone(new Milestone("Checkpoint Milestone", String.valueOf(currentCount) + " attendees now are checked in", String.valueOf(currentCount)));
            sendMilestoneNotification();
        }
    }

    /**
     * sends a notification to the organizer when a milestone is passed
     */
    void sendMilestoneNotification(){

        MainActivity.db.collection("Users").document(event.getOrganizerId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String fcmToken = documentSnapshot.getString("FCM");
                    if(fcmToken != null){
                        Log.d("fcm Token organizer ",fcmToken);
                        try {
                            notification.sendNotification(fcmToken,"New Milestone",event);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

    }

    /**
     * stores a new milestone in firestore
     * @param milestone the milestone to be put
     */
    public void setMilestone(Milestone milestone){
        CollectionReference milestoneRef = MainActivity.db.collection("Events/"+event.getId()+"/milestones");
        Map<String, Object> milestoneMap = new HashMap<>();
        milestoneMap.put("title", milestone.getTitle());
        milestoneMap.put("message", milestone.getMessage());
        milestoneMap.put("currentDateTime", milestone.getCurrentDateTime());
        milestoneMap.put("milestoneCount",milestone.getCount());
        milestoneMap.put("timestamp", FieldValue.serverTimestamp());

        milestoneRef.add(milestoneMap);

        Log.d("milestone adding","working");

    }

}
