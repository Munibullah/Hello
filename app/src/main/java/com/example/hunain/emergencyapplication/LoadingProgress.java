package com.example.hunain.emergencyapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.view.menu.ActionMenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.Timer;

/**
 * Created by hunain on 1/27/2018.
 */

public class LoadingProgress {
    ProgressBar p;
    Activity activity;
    Button b;
    int progressBarStatus = 0;
    Timer timer;
    Handler handler = new Handler();


    public LoadingProgress(ProgressBar p,Button b){
        this.b = b;
        this.p = p;
    }

    public void loading(){
        p.setVisibility(View.VISIBLE);
        b.setVisibility(View.GONE);
    }
    public  void stopLoading(){
        b.setVisibility(View.VISIBLE);
        p.setVisibility(View.GONE);
    }


}
