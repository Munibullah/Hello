package com.example.hunain.emergencyapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hunain.emergencyapplication.Business_Object.ITokenBAO;
import com.example.hunain.emergencyapplication.Business_Object.TokenBAO;
import com.example.hunain.emergencyapplication.Data_Access.TokenDAO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;

public class PasswordActivity extends AppCompatActivity {
    private static final String TAG = "PasswordActivity";
    private int ERROR_DIALOGUE_REQUEST = 9001;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
    }

    public void getPassword(View view)
    {
        password = (EditText) findViewById(R.id.password);
        String pass =(String) getIntent().getSerializableExtra("Get Password");
        String d = password.getText().toString();
        String userType = (String) getIntent().getSerializableExtra("User Type");
        if(d.equals(pass)){
            if(isServicesOk()) {
                if (userType.equals("User")) {
                    ITokenBAO token = new TokenBAO(new TokenDAO(getApplicationContext()),getApplicationContext());
                    String recent_token = FirebaseInstanceId.getInstance().getToken();
                    token.UpdateToken(recent_token);
                    Log.i("Method:getPassword()","triggered");
                    Intent i = new Intent(PasswordActivity.this, MapsActivity.class);
                    i.putExtra("CustomerID", ((int) getIntent().getSerializableExtra("getCustomerID")));
                    i.putExtra("getCustomerName",(String) getIntent().getSerializableExtra("customerName"));
                    i.putExtra("getCustomerPhoneNumber",(String) getIntent().getSerializableExtra("customerPhoneNumber"));
                    startActivity(i);
                    // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                }
                else if(userType.equals("FireFighter")){
                    Intent i = new Intent(PasswordActivity.this, FirestationActivity.class);

                    startActivity(i);
                }else
                {
                    Toast.makeText(getApplicationContext(),"wrong password",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public boolean isServicesOk(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(PasswordActivity.this);
        if(available == ConnectionResult.SUCCESS){
            //Everything is already and you get map request
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //error occured but we can resolve version issue
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(PasswordActivity.this,available,ERROR_DIALOGUE_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(this,"Map request cannot be possible",Toast.LENGTH_SHORT).show();
        }
        return  false;
    }


}
