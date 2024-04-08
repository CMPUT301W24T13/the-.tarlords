package com.example.the_tarlords.data.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Responsisble for displayed messages/notifications on the device
 */

public class FCMService extends FirebaseMessagingService {
// https://stackoverflow.com/questions/38451235/how-to-handle-the-firebase-notification-when-app-is-in-foreground

    /**
     * runs code when a message is received
     * @param message Remote message that has been received.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Map<String, String> data = message.getData();
        String eventId = data.get("event");

        MainActivity.db.collection("Events").document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Event event = documentSnapshot.toObject(Event.class);
                    notificationBuilder(message,event);

                }
            }
        });

    }

    /**
     * builds a notification
     * @param message message recieved from firebase
     * @param event event object
     */
    private void notificationBuilder(RemoteMessage message, Event event){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("event", event);
        if(message.getNotification().getBody().equals("New Announcement")){
            intent.setAction("OPEN_EVENT_DETAILS");

        } else if (message.getNotification().getBody().equals("New Milestone")){
            intent.setAction("OPEN_EVENT_DETAILS_ORGANIZER");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(message.getNotification().getTitle())
                .setContentText(message.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);
        manager.notify(0, builder.build());
    }



}
