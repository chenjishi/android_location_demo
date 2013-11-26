package com.location.demo.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.demo.LocationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

public class BootCompleteReceiver extends BroadcastReceiver implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	static final int ONE_MINUTE = 60000;
	Context context;
	LocationClient locationClient;
	LocationRequest locationRequest;

	@Override
	public void onReceive(Context context, Intent arg1) {
		this.context = context;
		int resp = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (resp == ConnectionResult.SUCCESS) {
			locationClient = new LocationClient(context, this, this);
			locationClient.connect();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		locationRequest = LocationRequest.create();
		locationRequest.setInterval(5 * ONE_MINUTE);
		locationRequest.setFastestInterval(5 * ONE_MINUTE);
		locationRequest
				.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

		Intent intent = new Intent(context, LocationIntentService.class);
		PendingIntent pIntent = PendingIntent.getService(context, 1, intent, 0);
		locationClient.requestLocationUpdates(locationRequest, pIntent);

		locationClient.disconnect();
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

}
