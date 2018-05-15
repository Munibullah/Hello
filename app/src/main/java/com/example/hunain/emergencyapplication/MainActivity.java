package com.example.hunain.emergencyapplication;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = (Button)(findViewById(R.id.startBtn));

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
               // ActivityOptions options = ActivityOptions.makeCustomAnimation(MainActivity.this,R.anim.fade_in,R.anim.fade_out);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();

            }
        });
    }

}
