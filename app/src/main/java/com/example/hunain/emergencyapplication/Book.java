package com.example.hunain.emergencyapplication;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hunain on 12/3/2017.
 */

public class Book implements Serializable{
    public int CID;
    public String phoneNumber;
    public String customerName;
    public String problem;
    public List<Integer> departmentId;
    public double longitude;
    public double latitude;
    public boolean requestStatus;
    public String customerDeviceToken;

}
