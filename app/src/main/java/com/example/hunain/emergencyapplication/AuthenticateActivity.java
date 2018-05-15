package com.example.hunain.emergencyapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.Serializable;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class AuthenticateActivity extends AppCompatActivity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        Button send = (Button)findViewById(R.id.send);

    }
    //Create method so that user can sign in
    public void signInWithPhone(PhoneAuthCredential credential){
        auth = FirebaseAuth.getInstance();

        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent i = new Intent(AuthenticateActivity.this, UserData.class);

                    String phoneNumber = (String)getIntent().getSerializableExtra("Phone Number");
                    startActivity(i.putExtra("Phone Number",phoneNumber));
                    // ActivityOptions options = ActivityOptions.makeCustomAnimation(MainActivity.this,R.anim.fade_in,R.anim.fade_out);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });
    }
    public void varifyCode(View view) {
        String varificationCode =(String) getIntent().getSerializableExtra("Varification Code");
        Log.i("Check varification Code",varificationCode);
        EditText inputPhoneCode = (EditText) findViewById(R.id.varificationCode);
        varifiyPhoneNumber(varificationCode,inputPhoneCode.getText().toString());


    }
    public void varifiyPhoneNumber(String varifyCode,String inputCode){
        PhoneAuthCredential credentials = PhoneAuthProvider.getCredential(varifyCode,inputCode);
        signInWithPhone(credentials);
    }
}
