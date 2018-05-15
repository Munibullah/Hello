package com.example.hunain.emergencyapplication.Business_Object;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.hunain.emergencyapplication.Common.ConnectivityHelper;
import com.example.hunain.emergencyapplication.Data_Access.IDataAccess;
import com.example.hunain.emergencyapplication.Data_Access.TokenDAO;
import com.example.hunain.emergencyapplication.Entity.Token;


/**
 * Created by hunain on 4/7/2018.
 */

public class TokenBAO implements  ITokenBAO {
    IDataAccess<Token> dataAccess;
    Context context;
    public TokenBAO(TokenDAO dataAccess, Context context){
        this.dataAccess = dataAccess;
        this.context = context;
    }


    @Override
    public void InsertToken(Token token) {

    }

    @Override
    public void UpdateToken(String token) {
        Token tokenEntity = new Token();
        if(ConnectivityHelper.isInternetConnectionAvailable(context)){
            Log.i("Method : UpdateToken()","Triggered");
            tokenEntity.serialNumber = ConnectivityHelper.getMacAddress(context);
            tokenEntity.token = token;
            dataAccess.update(tokenEntity);
        }else{
            Toast.makeText(context,"Network Not Available",Toast.LENGTH_SHORT).show();
        }
    }
}
