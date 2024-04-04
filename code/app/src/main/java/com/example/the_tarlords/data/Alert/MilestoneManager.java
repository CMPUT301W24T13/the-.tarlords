package com.example.the_tarlords.data.Alert;

import static java.lang.Math.floor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.event.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

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

    public MilestoneManager(Event event){
        this.event = event;
        this.maxCount = event.getMaxSignUps();
        this.currentCount = event.getCheckIns();

    }
    public void updateMilestone() {
        sendMilestoneNotification();

        if(currentCount == 1){
            setMilestone(new Milestone("First milestone","First attendee checked in",String.valueOf(currentCount)));
            sendMilestoneNotification();

        } else if (currentCount == maxCount / 2) {
            setMilestone(new Milestone("Half capacity milestone","Half of the attendees,"+String.valueOf(currentCount)+"/"+String.valueOf(maxCount)+", are checked in",String.valueOf(currentCount)));
            sendMilestoneNotification();

        }else if(currentCount >0 && currentCount % 5 == 0){
            setMilestone(new Milestone("Checkpoint Milestone",String.valueOf(currentCount)+" attendees now are checked in",String.valueOf(currentCount)));
            sendMilestoneNotification();

        }else if(currentCount == maxCount){
            setMilestone(new Milestone("Full capacity milestone","All attendees "+ String.valueOf(currentCount)+", now are checked in",String.valueOf(currentCount)));
            sendMilestoneNotification();

        }


    }

    void sendMilestoneNotification(){

        MainActivity.db.collection("Users").document(event.getOrganizerId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String fcmToken = documentSnapshot.getString("FCM");
                    if(fcmToken != null){
                        Log.d("fcm Token organizer ",fcmToken);
                        try {
                            JSONObject jsonObject = getJsonObject(fcmToken,"New Milestone");
                            callApi(jsonObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

    }
    public void setMilestone(Milestone milestone){
        CollectionReference milestoneRef = MainActivity.db.collection("Events/"+event.getId()+"/milestones");
        Map<String, Object> milestoneMap = new HashMap<>();
        milestoneMap.put("title", milestone.getTitle());
        milestoneMap.put("message", milestone.getMessage());
        milestoneMap.put("currentDateTime", milestone.getCurrentDateTime());
        milestoneMap.put("milestoneCount",milestone.getCount());
        milestoneRef.add(milestoneMap);

        Log.d("milestone adding","working");

    }

    /**
     * creates a JSONObject representation of a notification
     * @param fcmToken
     * @param text
     * @return JSONObject representation
     * @throws JSONException
     */
    @NonNull
    private static JSONObject getJsonObject(String fcmToken, String text) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONObject notificationObject = new JSONObject();
        JSONObject dataObject = new JSONObject();
        JSONObject androidPriorityObject = new JSONObject();
        notificationObject.put("title", event.getName());
        notificationObject.put("body",text);
        dataObject.put("event",event.getId());
        androidPriorityObject.put("priority","high");
        jsonObject.put("notification",notificationObject);
        jsonObject.put("data",dataObject);
        jsonObject.put("to", fcmToken);
        jsonObject.put("android",androidPriorityObject);
        return jsonObject;
    }
    void callApi(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(RequestBody.create(jsonObject.toString(),JSON))
                .header("Authorization", "Bearer AAAA9JmSg9Q:APA91bG_VZRBkbQa1whOowc_R2F1P8M_RUcDERhZa-YRM-EgSiAaoHBxSV4UO0bETyAvHh7d7P9fPjgIlfPqZcU-_xRKrIW71swZCu-uLSzdf6cravZN6zhs1HvtDt28afiwDevDnJ7b")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }


}
