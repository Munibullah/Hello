package com.example.hunain.emergencyapplication;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

/**
 * Created by hunain on 1/25/2018.
 */

public class CustomProgressBar {
    ProgressBar progressBar;
    int progressBarStatus = 0;
    int MAX_NUMBER = 100;
    Activity activity;

    public CustomProgressBar(Activity activity, ProgressBar progressBar){
        this.progressBar = progressBar;
        this.activity = activity;
    }


    public void loadingProgress(){
        Thread timer = new Thread(){
            public void run(){
                try{

                    while(progressBarStatus < MAX_NUMBER){
                        sleep(500);
                        activity.runOnUiThread(new Runnable(){
                            public void run()
                            {
                                //progressBar.setProgress(progressBarStatus);
                               smootherAnimation();
                                progressBarStatus += 20;
                            }
                        });

                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        timer.start();

    }

    public void smootherAnimation(){
        if(android.os.Build.VERSION.SDK_INT >= 11){
            // will update the "progress" propriety of seekbar until it reaches progress
            ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress",100,0);
            animation.setDuration(3500); // 0.5 second
            animation.setInterpolator(new LinearInterpolator());
            animation.start();
        }
    }
}
