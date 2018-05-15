package com.example.hunain.emergencyapplication;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserData extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText confirmPassword;
    RestService rs;
    Customer md;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        firstName = (EditText) findViewById(R.id.firstNameInput);
        lastName = (EditText) findViewById(R.id.lastNameInput);
        email = (EditText) findViewById(R.id.emailInput);
        password = (EditText) findViewById(R.id.passwordInput);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
    }


    public void getData(View view){
         md = new Customer();
        md.phoneNumber = (String) getIntent().getSerializableExtra("Phone Number");
        md.name = firstName.getText().toString() + lastName.getText().toString();
        md.email = email.getText().toString();
        md.password = password.getText().toString();
        md.userType = "User";

        rs = new RestService();
        Call<Integer> customer = rs.getService().addCustomer(md);
        customer.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(password.getText().toString().equals(confirmPassword.getText().toString()) ) {
                    Toast.makeText(getApplicationContext(), "Save succefully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UserData.this, MapsActivity.class);
                    i.putExtra("getCustomerName",(String) md.name);
                    i.putExtra("getCustomerPhoneNumber",(String) md.phoneNumber);
                    i.putExtra("CustomerID", ((int) response.body()));
                    startActivity(i);
                }else {
                    Toast.makeText(getApplicationContext(), "unmatch password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });


    }



}
