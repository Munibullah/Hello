package com.example.hunain.emergencyapplication.Business_Object;


import com.example.hunain.emergencyapplication.Entity.Token;

/**
 * Created by hunain on 4/7/2018.
 */

public interface ITokenBAO {
    void InsertToken(Token token);
    void UpdateToken(String token);
}
