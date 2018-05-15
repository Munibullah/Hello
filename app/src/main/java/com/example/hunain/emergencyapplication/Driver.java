package com.example.hunain.emergencyapplication;

import java.io.Serializable;

/**
 * Created by hunain on 1/1/2018.
 */

public class Driver implements Serializable {
    public int Id ;
    public String driverName;
    public String departmentName;
    public String branchName;
    public double latitude;
    public double longitude;
    public String deviceToken;
    public String phoneNumber;
    public boolean isAcceptRequest;


}
