package com.example.hunain.emergencyapplication;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by hunain on 12/10/2017.
 */

public class FirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {
    private static final String REG_TOKEN = "REG_TOKEN";
    @Override
    public void onTokenRefresh() {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.i(REG_TOKEN,recent_token);
        /*
        if your app in background your application will recieve a token  on
        device notification claim other if the application in foreground in order to recieve notification
        and data from firebase you need to create another java class that extend firebase messaging service
        */
    }
}
