package com.example.demo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.ActivityRecognitionClient;

/**
 * Created with IntelliJ IDEA.
 * User: chenjishi
 * Date: 13-7-19
 * Time: 下午3:26
 * To change this template use File | Settings | File Templates.
 */
public class ActivityRecognitionActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    private static final long FIVE_MINUTES = 5 * 60 * 1000;

    private ActivityRecognitionClient activityRecognitionClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityRecognitionClient = new ActivityRecognitionClient(this, this, this);
        activityRecognitionClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Intent intent = new Intent(this, ActivityRecIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        activityRecognitionClient.requestActivityUpdates(FIVE_MINUTES, pendingIntent);
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityRecognitionClient.disconnect();
    }
}
