package com.example.hunain.emergencyapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirestationActivity extends AppCompatActivity {
    ListView viewRequests;
    ArrayList<Integer> listViewContent;
    ArrayAdapter<RequestItems> arrayAdapter;
    //private BroadcastReceiver mReceiver;
    Book book;
   /* BroadcastReceiver   mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //extract our message from intent
            // Intent myIntent = getIntent();
            // Bundle args = intent.getBundleExtra("DATA");
            // book  = (Book)  intent.getExtras().getParcelable("my Requests");
            book  = (Book)  intent.getSerializableExtra("my Requests");
            //log our message value
//registering our receiver
            //  this.registerReceiver(mReceiver, intentFilter);
            listViewContent.add(book.CID);
            arrayAdapter.notifyDataSetChanged();
            //log our message value

        }
    };*/
   @Override
    protected void onResume() {
        super.onResume();
       // IntentFilter intentFilter = new IntentFilter("user requests");

        //registering our receiver
      //  this.registerReceiver(mReceiver, intentFilter);
        //listViewContent.add(book.CID);
       // arrayAdapter.notifyDataSetChanged();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestation);
        viewRequests = (ListView)findViewById(R.id.FireRequestView);
        listViewContent = new ArrayList<Integer>();
       // listViewContent.add(requestItems.name);
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listViewContent);

        viewRequests.setAdapter(arrayAdapter);
        //Handler handler = new Handler();








    }


    public void listOfRequest(){
        RestService rs = new RestService();
        Call<GetRequest> r = rs.getService().getRequests(1);
        r.enqueue(new Callback<GetRequest>() {
            @Override
            public void onResponse(Call<GetRequest> call, Response<GetRequest> response) {
                GetRequest g = new GetRequest();
                //RequestItems requestItems = new RequestItems();
                g.phoneNumber =response.body().phoneNumber;
                g.problem = response.body().phoneNumber;
                g.departmentName = response.body().phoneNumber;
                g.latitude = response.body().latitude;
                g.longitude = response.body().longitude;

                g.requestStatus = response.body().requestStatus;
                g.name = response.body().name;

               // addItems(g);

            }

            @Override
            public void onFailure(Call<GetRequest> call, Throwable t) {

            }
        });
    }
   /* public void addItems(GetRequest r){
        RequestItems requestItems = new RequestItems();
        requestItems.name = r.name;
        requestItems.problem = r.problem;
        listViewContent.add(requestItems.name);
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listViewContent);
       arrayAdapter.notifyDataSetChanged();
        viewRequests.setAdapter(arrayAdapter);


    }*/
}

class RequestItems{
    String name;
    String problem;
}
