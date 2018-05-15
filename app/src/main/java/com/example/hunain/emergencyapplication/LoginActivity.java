package com.example.hunain.emergencyapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.transition.CircularPropagation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ProgressBar progressButton,p;
    FirebaseAuth auth;
    EditText editTextCarrierNumber;
    CountryCodePicker ccp;
    String varificationCode;
    RestService rs;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    String phoneNumber;
    Customer customer;
    String password;
    int customerID;
    boolean isNumberFound;
    Button loginBtn;
    int progressBarStatus = 0;
    LoadingProgress loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authentications();
        initilizeInstances();

        //ProgressBar horizontalProgressBar = (ProgressBar) findViewById(R.id.horizontalProgressBar);
        //CustomProgressBar progressBar = new CustomProgressBar(this,horizontalProgressBar);
       // progressBar.loadingProgress();


    }









    public void initilizeInstances()
    {
        ccp = (CountryCodePicker)findViewById(R.id.ccp);
        editTextCarrierNumber = (EditText)(findViewById(R.id.editText_carrierNumber));
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);
        auth = FirebaseAuth.getInstance();
        customer = new Customer();
        progressButton = (ProgressBar) findViewById(R.id.progress);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loader = new LoadingProgress(progressButton,loginBtn);

    }
    public void authentications() {

        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                    Log.i("Phone Number Failed","Wrong Phone number formate");


            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                //super.onCodeSent(s, forceResendingToken);
                varificationCode = s;
                loader.stopLoading();
                sendCrediantial();
            }
        };
    }
    public  void sendSms(){
        //rs = new RestService();
        phoneNumber = ccp.getFullNumber().toString();
        Call<Customer> result =new RestService().getService().getCustomerData(phoneNumber);
        result.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                customer.ID = response.body().ID;
                customer.name = response.body().name;
                password = response.body().password;
                customer.email = response.body().email;
                customer.phoneNumber = response.body().phoneNumber;
                customer.userType = response.body().userType;
                // = Customer.password;

                if(phoneNumber.equals(customer.phoneNumber)){
                    loader.stopLoading();
                   userLogin();
                }
                else {

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(ccp.getFullNumberWithPlus().toString(), 60, TimeUnit.SECONDS, LoginActivity.this,mCallBack);
                   // Intent i = new Intent(LoginActivity.this, UserData.class);
                    //i.putExtra("Varification Code",varificationCode);
                    //i.putExtra("Phone Number",phoneNumber);

                    //startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.i("Message",t.getMessage());

            }
        });





    }
    public void sendCrediantial(){
        Intent i = new Intent(LoginActivity.this, AuthenticateActivity.class);
        i.putExtra("Varification Code",varificationCode);
        i.putExtra("Phone Number",phoneNumber);

        startActivity(i);
        // ActivityOptions options = ActivityOptions.makeCustomAnimation(MainActivity.this,R.anim.fade_in,R.anim.fade_out);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }


    public  void userLogin(){
        Intent i = new Intent(LoginActivity.this, PasswordActivity.class);
        i.putExtra("Get Password",password);
        i.putExtra("customerName",customer.name);
        i.putExtra("customerPhoneNumber",customer.phoneNumber);
        i.putExtra("User Type",customer.userType);
        // customerID = customer.ID;
        i.putExtra("getCustomerID",customer.ID);

        startActivity(i);
        // ActivityOptions options = ActivityOptions.makeCustomAnimation(MainActivity.this,R.anim.fade_in,R.anim.fade_out);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void login(View view){

        loader.loading();
        sendSms();
    }




}
