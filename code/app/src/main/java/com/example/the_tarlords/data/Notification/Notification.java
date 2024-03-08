package com.example.the_tarlords.data.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.example.the_tarlords.R;

public class Notification {
    private Context context;

    public Notification(Context context){
        this.context = context;

    }

    public void sendNotification(){
        String title = "Notification title";
        String body = "New Notification";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel("channel","channel name",importance);
        notificationChannel.setDescription(body);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(importance);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(1,builder.build()); // need to get notification permissions from device
    }

}
