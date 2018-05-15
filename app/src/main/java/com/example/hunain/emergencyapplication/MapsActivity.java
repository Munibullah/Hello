package com.example.hunain.emergencyapplication;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.hunain.emergencyapplication.Common.Constants;
import com.example.hunain.emergencyapplication.Receiver.DirectionReciever;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,RoutingListener{
    private GoogleMap mMap;
    private LocationManager lm;
    private LocationRequest mLocationRequest;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    Button continueBtn;
    Button cancelBtn;
    // FusedLocationProviderClient mFusedLocationClient;
    String provider;
    private static String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 15f;
    Location currentLocation;
    private static final int SECOND_ACTIVITY_RESULT_CODE = 0;
    LatLngBounds.Builder boundsBuilder;
    private List<Polyline> polylines;
    Driver driver;
    TextView name,branchName,department,phoneNumber;
    RestService rs;
    RelativeLayout panel;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            //Book book =(Book) intent.getSerializableExtra("request");


            if(RequestStatus.isRequestAccept && "Emergency".equals(RequestStatus.title) ){
               //View b = findViewById(R.id.dpanel);
               requestHandler(intent);

            }
            else if(!RequestStatus.isRequestAccept && "cancel".equals(RequestStatus.title)) {
                erasePolyLine();
                removeMarkers();
                reset();
                stateChanged(true);
            }


            //do other stuff here

        }
    };
    private IntentFilter intentFilter = new IntentFilter("myFunction");


    public  void requestHandler(Intent intent){
        driver  = (Driver) intent.getSerializableExtra("request");
        driverDetails(driver);
        panel.setVisibility(View.VISIBLE);
        panel.invalidate();
        routeToTheMarker();
        stateChanged(false);
        cancelBtn.setVisibility(View.VISIBLE);
        continueBtn.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(RequestStatus.isRequestAccept && "Emergency".equals(RequestStatus.title)){
          //  RequestStatus.isRequestAccept = false;
            requestHandler(getIntent());
                //requestPanel();}
        }else if(!(RequestStatus.isRequestAccept)  && ("cancel".equals(RequestStatus.title))){
            erasePolyLine();
            removeMarkers();
            reset();
            stateChanged(true);


        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,intentFilter);

    }
    public void removeMarkers(){
        mMap.clear();
    }

    public void stateChanged(boolean state){
        RequestStatus.isRequestAccept = state;
    }
    public void reset(){
        cancelBtn.setVisibility(View.GONE);
        continueBtn.setVisibility(View.VISIBLE);
        if(mLocationGranted){
            getDeviceLocation();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getLocationPermission();
       //panel.animate().rotationYBy(-500f).setDuration(1000);


        continueBtn = (Button) findViewById(R.id.continueBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        boundsBuilder = new LatLngBounds.Builder();
        polylines = new ArrayList<Polyline>();

        panel = (RelativeLayout) findViewById(R.id.dpanel);
        //View b = findViewById(R.id.button);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /*
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = lm.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(provider);
        if (location != null) {
            onLocationChanged(location);
        }
        */



    }
    public  void routeToTheMarker(){
         /*   Constants.driverLocation = new LatLng(driver.latitude,driver.longitude);
        Intent intent = new Intent(this, DirectionReciever.class);
*/


        // final PendingIntent pIntent = PendingIntent.getBroadcast(this, DirectionReciever.REQUEST_CODE, intentFilesSynchronizer, PendingIntent.FLAG_UPDATE_CURRENT);
        //startService(intent);
       /* double latitude = currentLocation.getLatitude();
        mMap.addMarker(new MarkerOptions().position(new LatLng(driver.latitude, driver.longitude)).title("Driver Location"));
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()), new LatLng(driver.latitude,driver.longitude))
                .build();
        routing.execute();


        boundsBuilder.include(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));
        boundsBuilder.include( new LatLng(driver.latitude,driver.longitude));*/

      // Directions directions = new Directions(this,mMap,Constants.userLocation,Constants.driverCurrentLocation,new LatLng(driver.latitude,driver.longitude),)
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       //panel = (RelativeLayout) findViewById(R.id.customDriverPanel);

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (mLocationGranted) {
            getDeviceLocation();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
       // mMap.setMyLocationEnabled(true);
    }


    public void getLocationPermission() {
        String[] permission = {FINE_LOCATION, COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Complete location found
                        Log.i("LocationFinding","triggered");
                            currentLocation = (Location) task.getResult();
                            Constants.userLocation = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                            Constants.userMarker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()))
                                    .title("Current Location"));
                            Log.i("LocationFound","add Marker");
                            // we take the result and move the camera to get result
                            //im gonna create method to move a method

                           moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);

                        } else {
                            Toast.makeText(MapsActivity.this, "unable to get location", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("getDeviceLocation: ", e.getMessage());

        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            mLocationGranted = false;
                            return;
                        }
                    }
                    mLocationGranted = true;

                }
            }
        }
    }
    public void book(View view){
        Intent i = new Intent(MapsActivity.this, FormActivity.class);
        i.putExtra("Latitute",currentLocation.getLatitude());
        i.putExtra("Longitude",currentLocation.getLongitude());
        i.putExtra("CustomerID",((int)getIntent().getSerializableExtra("CustomerID")));
        i.putExtra("customerPhoneNumber",(String) getIntent().getSerializableExtra("getCustomerPhoneNumber"));
        i.putExtra("customerName",(String) getIntent().getSerializableExtra("getCustomerName"));
        startActivityForResult(i,SECOND_ACTIVITY_RESULT_CODE);
        progress();



        // ActivityOptions options = ActivityOptions.makeCustomAnimation(MainActivity.this,R.anim.fade_in,R.anim.fade_out);

    }
    public void progress(){
        ProgressDialog progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setMessage("Searching please wait.....");
        progressDialog.show();
    }

    public void cancelProblem(View view){
        //Retrofit code here
       // int customerId = ((int)getIntent().getSerializableExtra("CustomerID"));

        Call<String> cancelRequestSend = new RestService().getService().cancelRequestToDriver(driver.Id);
        cancelRequestSend.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                erasePolyLine();
                removeMarkers();
                panel.setVisibility(View.GONE);
                reset();
                continueBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });








    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SECOND_ACTIVITY_RESULT_CODE && resultCode == SECOND_ACTIVITY_RESULT_CODE){
            continueBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.VISIBLE);
        }
        /*if(resultCode == SECOND_ACTIVITY_RESULT_CODE){
                rs = new RestService();
            Call<List<Department>> rDepartment  = rs.getService().getRegionalDepartment();
            rDepartment.enqueue(new Callback<List<Department>>() {
                @Override
                public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                    for(Department d : response.body())
                    {
                    }
                }

                @Override
                public void onFailure(Call<List<Department>> call, Throwable t) {

                }
            });
        }*/

    }
    public  void logout(View view){
        Intent intent = new Intent(MapsActivity.this,
                MainActivity.class);
        //intent.set;Flags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent);

    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);
            int routePadding = 100;
            LatLngBounds latLngBounds = boundsBuilder.build();

            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200));


            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }
    private  void erasePolyLine() {
        if (!polylines.isEmpty()) {
            for (Polyline line : polylines) {
                line.remove();
            }
            polylines.clear();
        }
    }
    public void driverDetails(Driver d){


        name = (TextView) findViewById(R.id.driverName);
        department = (TextView) findViewById(R.id.driverDepartment);
       branchName = (TextView)findViewById(R.id.branchName);
        phoneNumber = (TextView) findViewById(R.id.driverPhoneNumber);

        name.setText("Driver Name: " + d.driverName);
        department.setText("Department: " +d.departmentName);
        branchName.setText("Branch Name: " +d.branchName);
        phoneNumber.setText("Phone Number : " +d.phoneNumber);





//        name.setText(driver.driverName);
//        department.setText(driver.departmentName);
//        branchName.setText(driver.branchName);
//        phoneNumber.setText(driver.phoneNumber);

        //panel.animate().translationYBy(500f).setDuration(1000);


    }
}




