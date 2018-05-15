package com.example.hunain.emergencyapplication.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.hunain.emergencyapplication.Common.ConnectivityHelper;
import com.example.hunain.emergencyapplication.Service.DirectionService;


/**
 * Created by hunain on 4/26/2018.
 */

public class DirectionReciever extends BroadcastReceiver {
    public static int REQUEST_CODE = 1002;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mIntent = new Intent(context, DirectionService.class);

        if(ConnectivityHelper.isInternetConnectionAvailable(context)){
            context.startService(mIntent);
        }
    }
}
