package com.example.hunain.emergencyapplication.Service;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import com.example.hunain.emergencyapplication.Common.Constants;
import com.example.hunain.emergencyapplication.Directions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by hunain on 4/21/2018.
 */

public class DirectionService extends Service implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

        GoogleApiClient mGoogleApiClient;
        LocationRequest mLocationRequest;
        int INTERVAL = 10000, FASTEST_INTERVAL = 5000;
    Directions direction;




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(!mGoogleApiClient.isConnected()){
            mGoogleApiClient.connect();
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createLocationRequest();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(Constants.userMarker == null) {
            Constants.userMarker = Constants.map.addMarker(new MarkerOptions()
                    .position(Constants.userLocation)
                    .title("Current Location"));
        }
        startLocationUpdates();
        direction = new Directions(this,Constants.map,Constants.userLocation,Constants.driverLocation,Constants.userMarker);
        direction.getDirections();
        //System.out.println(Toast.makeText(getApplicationContext(), "On Connected runs", Toast.LENGTH_SHORT));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location driverNewlocation) {
       /*direction.moveMarker(Constants.driverCurrentLocation,new LatLng(driverNewlocation.getLatitude(),driverNewlocation.getLatitude()),false);
        Constants.driverCurrentLocation = new LatLng(driverNewlocation.getLatitude(),driverNewlocation.getLongitude());*/
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
    }


    public synchronized  void googleApiCientConnection() {
        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Intent intent = new Intent(this, DirectionService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, pendingIntent);
        Log.d("Location Updates", "Location update started ..............: ");
    }
    protected void stopLocationUpdates() {
        Intent intent = new Intent(this, DirectionService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, pendingIntent);

        Log.d("Location Updates", "Location update stopped .......................");
    }





}
