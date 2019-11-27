package com.example.apprpg.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.apprpg.R;
import com.example.apprpg.ui.activities.FirstActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null){
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            pushNotification(title, body);
        }

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    private void pushNotification(String title, String body){

        Intent intent = new Intent(this, FirstActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String strChannel = getResources().getString(R.string.default_notification_channel_id);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, strChannel)
                .setContentTitle(title)
                //.setContentText(body)
                .setSmallIcon(R.drawable.ic_photo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_panorama))
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body));
                //.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(strChannel, "channel", NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(0, notification.build());
        }

    }
}
