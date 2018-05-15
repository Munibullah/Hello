package com.example.hunain.emergencyapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.hunain.emergencyapplication.Common.Constants;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by hunain on 12/10/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       showNotification(remoteMessage);

    }
    public  void showNotification(RemoteMessage message) {
        Map<String, String> data = message.getData();
        String body = data.get("body");
        Gson gson = new Gson();
        Intent intnt = new Intent("myFunction");
        if (data.get("title").equals("cancel")) {
            RequestStatus.isRequestAccept = false;
            RequestStatus.title = data.get("title");
            intnt.putExtra("request","cancel");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intnt);

        } else if (data.get("title").equals("Emergency")) {
            RequestStatus.title = data.get("title");
            Driver driverRequest = gson.fromJson(body, Driver.class);
            RequestStatus.isRequestAccept = true;
            if (!myApplication.isActivityVisible()) {

                intnt.putExtra("request", driverRequest);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intnt);

            }else if(data.get("title").equals("track")){
                Constants.driverLocation = gson.fromJson(body, LatLng.class);
            }

            else {

               sendNotification(driverRequest);
            }

        }
    }
    public void sendNotification(Driver driverRequest){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("request", driverRequest);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        long[] pattern = {500, 500, 500, 500, 500};
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this, "default");
        mNotificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(driverRequest.departmentName)
                .setContentText("Emergency Driver for the request " + driverRequest.driverName + " from " + driverRequest.branchName + " is on its way")
                .setVibrate(pattern)
                .setAutoCancel(true)
                .setLights(Color.RED, 1, 1)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
}
