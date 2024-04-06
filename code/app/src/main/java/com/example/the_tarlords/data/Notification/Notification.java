package com.example.the_tarlords.data.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Notification {
    /**
     * constructor for the notification class
     */
    public Notification(){
    }

    /**
     * sends a notification to the fcm token
     * @param fcmToken receiver
     * @param text text of the notification
     * @param event the corresponding event
     * @throws JSONException
     */
    public void sendNotification(String fcmToken,String text, Event event) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONObject notificationObject = new JSONObject();
        JSONObject dataObject = new JSONObject();
        JSONObject androidPriorityObject = new JSONObject();
        notificationObject.put("title", event.getName());
        notificationObject.put("body", text);
        dataObject.put("event", event.getId());
        androidPriorityObject.put("priority", "high");
        jsonObject.put("notification", notificationObject);
        jsonObject.put("data", dataObject);
        jsonObject.put("to", fcmToken);
        jsonObject.put("android", androidPriorityObject);

        callApi(jsonObject);

    }

    /**
     * sends the notification using the OkHttpClient api
     * @param jsonObject a jsonObject representing a notification
     */
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
