package com.example.hunain.emergencyapplication.Data_Access;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.example.hunain.emergencyapplication.Entity.Token;
import com.example.hunain.emergencyapplication.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hunain on 4/7/2018.
 */

public class TokenDAO implements IDataAccess<Token> {

    Context context;

    public TokenDAO(Context context){
        this.context = context;
    }

    @Override
    public void insert(Token entity) {

    }

    @Override
    public void update(final Token entity) {
        Log.i("Method : update","Triggered");
        Call<String> call = new RestService().getService().UpdateToken(entity);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Method : UpdateToken()","onResponseMethod successfully called");
               /* String previousToken =  StoreToken.getInstance(context).getToken();
                StoreToken.getInstance(context).remove(previousToken);
                StoreToken.getInstance(context).storeToken(entity.token);*/
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context,"not successfully updated",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
