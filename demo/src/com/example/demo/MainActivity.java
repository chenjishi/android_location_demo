package com.example.demo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationStatusCodes;

import java.util.ArrayList;

public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationClient.OnAddGeofencesResultListener {

    private LocationClient locationClient;
    private LocationRequest locationRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resp == ConnectionResult.SUCCESS) {
            locationClient = new LocationClient(this, this, this);
            locationClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5 * 50 * 1000);
        locationRequest.setFastestInterval(5 * 50 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        Intent intent = new Intent(this, LocationIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, 0);
        locationClient.requestLocationUpdates(locationRequest, pendingIntent);

        ArrayList<Store> storeList = getStoreList();
        if (null != storeList && storeList.size() > 0) {
            ArrayList<Geofence> geofenceList = new ArrayList<Geofence>();
            for (Store store : storeList) {
                float radius = (float) store.radius;
                Geofence geofence = new Geofence.Builder()
                        .setRequestId(store.id)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                        .setCircularRegion(store.latitude, store.longitude, radius)
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .build();

                geofenceList.add(geofence);
            }

            PendingIntent geoFencePendingIntent = PendingIntent.getService(this, 0,
                    new Intent(this, GeofenceIntentService.class), PendingIntent.FLAG_UPDATE_CURRENT);
            locationClient.addGeofences(geofenceList, geoFencePendingIntent, this);
        }
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onAddGeofencesResult(int i, String[] strings) {
        if (LocationStatusCodes.SUCCESS == i) {
            //todo check geofence status
        } else {

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    private ArrayList<Store> getStoreList() {
        ArrayList<Store> storeList = new ArrayList<Store>();
        for (int i = 0; i < 20; i++) {
            Store store = new Store();
            store.id = String.valueOf(i);
            store.address = "Beijing, China";
            store.latitude = 32.124455D;
            store.longitude = -121.3445223D;
            store.radius = 100.0D;

            storeList.add(store);
        }

        return storeList;
    }

    public class Store {
        String id;
        String address;
        double latitude;
        double longitude;
        double radius;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            locationClient.disconnect();
        }
    }
}
