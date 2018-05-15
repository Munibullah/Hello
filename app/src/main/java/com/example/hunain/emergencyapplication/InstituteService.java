package com.example.hunain.emergencyapplication;
import com.example.hunain.emergencyapplication.Entity.Token;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hunain on 11/17/2017.
 */

public interface InstituteService {


    //i.e. http://localhost/api/institute/Students/1
    //Get student record base on ID
    @GET("Customer/getCustomerData/")
//    public void getStudentById(@Path("phoneNumber")String phoneNumber,Callback<UserLogin> callback);
    Call<Customer> getCustomerData(@Query("phoneNumber") String phoneNumber);


   @POST("Customer/addCustomerData/")
    Call<Integer> addCustomer (@Body Customer customer);



    @POST("Request/sendRequest/")
    Call<String> sendRequest(@Body Book request);


    @GET("Department/getRegionalDepartment")
    Call<List<Department>> getRegionalDepartment();



    @GET("GetRequest/getRequests/")
    Call<GetRequest> getRequests(@Query("id")int id);



    @POST("Request/cancelRequestToDriver/")
    Call<String> cancelRequestToDriver(@Query("driverId") int driverId);


    @POST ("Tracking/CustomerLocation/")
    Call<String> CustomerLocation(@Query("driverLocation")LatLng customerLocation, @Query("token")String token);


    @PUT("Token/UpdateToken/{token}")
    Call<String> UpdateToken(@Body Token token);


    //i.e. http://localhost/api/institute/Students/1
    //Delete student record base on ID

}
