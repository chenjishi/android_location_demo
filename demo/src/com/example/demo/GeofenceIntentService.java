package com.example.demo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chenjishi
 * Date: 13-7-19
 * Time: 下午2:48
 * To change this template use File | Settings | File Templates.
 */
public class GeofenceIntentService extends IntentService {
    public static final String TRANSITION_INTENT_SERVICE = "ReceiveTransitionsIntentService";

    public GeofenceIntentService() {
        super(TRANSITION_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (LocationClient.hasError(intent)) {
            //todo error process
        } else {
            int transitionType = LocationClient.getGeofenceTransition(intent);
            if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER ||
                    transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
                List<Geofence> triggerList = LocationClient.getTriggeringGeofences(intent);

                for (Geofence geofence : triggerList) {
                    generateNotification(geofence.getRequestId(), "address you defined");
                }
            }
        }
    }

    private void generateNotification(String locationId, String address) {
        long when = System.currentTimeMillis();
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.putExtra("id", locationId);
        notifyIntent.putExtra("address", address);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.dac_logo)
                        .setContentTitle(locationId)
                        .setContentText(address)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setWhen(when);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) when, builder.build());
    }
}
