package com.example.hunain.emergencyapplication.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.hunain.emergencyapplication.Common.ConnectivityHelper;

import com.example.hunain.emergencyapplication.Service.LocationSyncService;
import com.google.android.gms.location.LocationServices;

/**
 * Created by hunain on 3/25/2018.
 */

public class UpdateLocationReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mIntent = new Intent(context, LocationServices.class);

        if(ConnectivityHelper.isInternetConnectionAvailable(context)){
            context.startService(mIntent);
        }
    }
}
