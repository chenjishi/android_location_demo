package com.example.demo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created with IntelliJ IDEA.
 * User: chenjishi
 * Date: 13-7-19
 * Time: 下午3:02
 * To change this template use File | Settings | File Templates.
 */
public class ActivityRecIntentService extends IntentService {
    public static final String ACTIVITY_RECOGNITION_INTENT_SERVICE = "ActivityRecognitionService";

    public ActivityRecIntentService() {
        super(ACTIVITY_RECOGNITION_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            DetectedActivity mostProbableActivity = result.getMostProbableActivity();

            int confidence = mostProbableActivity.getConfidence();
            int activityType = mostProbableActivity.getType();

            String type;
            if (activityType == DetectedActivity.IN_VEHICLE) {
                type = "In Car";
            } else if (activityType == DetectedActivity.ON_FOOT) {
                type = "On Foot";
            } else if (activityType == DetectedActivity.ON_BICYCLE) {
                type = "By Bicycle";
            } else {
                type = "Unknown";
            }

            generateNotification("Activity Recognition", type);
        }
    }

    private void generateNotification(String title, String content) {
        long when = System.currentTimeMillis();
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.putExtra("title", title);
        notifyIntent.putExtra("content", content);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.dac_logo)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setWhen(when);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) when, builder.build());
    }
}
