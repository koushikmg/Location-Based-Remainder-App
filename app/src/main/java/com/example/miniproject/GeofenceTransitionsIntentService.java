package com.example.miniproject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {
    protected static final String TAG = "GeofenceTransitionsIS";



    public GeofenceTransitionsIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event.hasError()) {
            Log.e(TAG, "GeofencingEvent Error: " + event.getErrorCode());
            return;
        }
        String description = getGeofenceTransitionDetails(event);
        Log.d(TAG, "onHandleIntent: "+event);
        createNotificationChannel();
        sendNotification(description);
    }



    private static String getGeofenceTransitionDetails(GeofencingEvent event) {
        String transitionString =
                GeofenceStatusCodes.getStatusCodeString(event.getGeofenceTransition());
        Log.d(TAG, "getGeofenceTransitionDetails: "+transitionString);
        List triggeringIDs = new ArrayList();
        for (Geofence geofence : event.getTriggeringGeofences()) {
            triggeringIDs.add(geofence.getRequestId());
            Log.d(TAG, "getGeofenceTransitionDetails: "+geofence);
        }
        return String.format("%s: %s", "Message  ", TextUtils.join(", ", triggeringIDs));
    }



    private void sendNotification(String notificationDetails) {
        Log.d(TAG, "sendNotification: " + notificationDetails);
        // Create an explicit content Intent that starts MainActivity.
        Intent notificationIntent = new Intent(getApplicationContext(), RemainderActivity.class);

        // Get a PendingIntent containing the entire back stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(RemainderActivity.class).addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"ID");
        long[] pattern = {500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,5000,500};
        // Define the notification settings.
        builder.setColor(Color.WHITE)
                .setSmallIcon(R.drawable.location_map_pin_turquoise_blue8)
                .setContentTitle(notificationDetails)
                .setContentText("Click notification to return to App")
                .setContentIntent(notificationPendingIntent)
                //.setAutoCancel(true)
                .setVibrate(pattern )
                .setLights(Notification.DEFAULT_LIGHTS,1000,500)
                .setDefaults(Notification.DEFAULT_SOUND);
        // .setFullScreenIntent(notificationPendingIntent,true);




        // Fire and notify the built Notification.
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
