package com.example.hunain.emergencyapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hunain.emergencyapplication.Common.StoreToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormActivity extends AppCompatActivity {

   List<Integer> departments;
    //String[] fireEmergency = {};
    String[] allproblems = {"Select Problem","Dehshat gardi","Dakaiti","Bhatta wasooli","Larki ko chera ho","Fire Burn","Road Accident","Flood","Accident injury","Beemari","Some other injury"};
   // String[] ambulance = {};
    Spinner sp;
    Spinner problems;
    Book book;
    RestService rs;
    Customer customer;
    HashMap<String,Integer> departmentId;
    CheckBox police;
    CheckBox fireFighter;
    CheckBox ambulance;
    private static final int First_ACTIVITY_RESULT_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
      //  sp = (Spinner)findViewById(R.id.SelectDepartment);

        problems = (Spinner) findViewById(R.id.SelectProblem);
        book = new Book();
        rs = new RestService();
        departments = new ArrayList<Integer>();
       createSpinner(R.layout.support_simple_spinner_dropdown_item,allproblems,problems);
       police = (CheckBox) findViewById(R.id.Police);
       ambulance = (CheckBox) findViewById(R.id.Ambulance);
       fireFighter = (CheckBox) findViewById(R.id.FireFighter);
        getAllDepartmentId();




    }
    public  void selectDepartments(View view){
        boolean checked = ((CheckBox) view).isChecked();

        if(view.getId() == R.id.Ambulance) {
            if (checked) {

                departments.add(departmentId.get(ambulance.getText().toString()));
            } else {
                departments.remove(departmentId.get(ambulance.getText().toString()));
            }
        }
       else if(view.getId() == R.id.FireFighter) {
            if (checked) {

                departments.add(departmentId.get(fireFighter.getText().toString()));
            } else {
                departments.remove(departmentId.get(fireFighter.getText().toString()));
            }
        }
       else if(view.getId() == R.id.Police) {
            if (checked) {

                departments.add(departmentId.get(police.getText().toString()));
            } else {
                departments.remove(departmentId.get(police.getText().toString()));
            }
        }



    }
    public void getAllDepartmentId(){
        departmentId = new HashMap<String,Integer>();
        departmentId.put("Fire Station",1);
        departmentId.put("Police",2);
        departmentId.put("Ambulance",3);

    }
    public void createSpinner(int spinnerLayoutType,String[] list,Spinner spinner){

        ArrayAdapter<String> array = new ArrayAdapter<String>(this,spinnerLayoutType,list);
        array.setDropDownViewResource(spinnerLayoutType);

        spinner.setAdapter(array);
    }
    public void Save(View view){
        Bundle data = getIntent().getExtras();

        book.latitude = (double) getIntent().getSerializableExtra("Latitute");
        book.longitude = (double) getIntent().getSerializableExtra("Longitude");
        book.problem = problems.getSelectedItem().toString();
      book.departmentId = departments;
        book.CID = (int) getIntent().getSerializableExtra("CustomerID");
        book.phoneNumber = (String) getIntent().getSerializableExtra("customerPhoneNumber");
        book.customerName = (String) getIntent().getSerializableExtra("customerName");
        book.customerDeviceToken = StoreToken.getInstance(getApplicationContext()).getToken();
        book.requestStatus = false;

        //int id = 1;
       // Intent i = new Intent();
        //i.setAction("com.example.hunain.emergencydriverapp");
        //i.putExtra("request",book);
        //i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
       //sendBroadcast(i);
       //exitActivity();
        Call<String> b = rs.getService().sendRequest(book);
        b.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(FormActivity.this, "send Successfully", Toast.LENGTH_SHORT).show();

                exitActivity();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });



    }
    public void sendNotification(Book request) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        long[] pattern = {500, 500, 500, 500, 500};
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this,"default");
                mNotificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("EMERGENCY")
                .setContentText("abc")
                .setVibrate(pattern)
                .setAutoCancel(true)
                .setLights(Color.RED, 1, 1)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
       // getApplicationContext().startActivity(intent);
    }
    public void exitActivity(){
        Intent intent = new Intent();
       // intent.putExtra("Request",)
        setResult(0);
        finish();



    }




}
