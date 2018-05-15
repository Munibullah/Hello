package com.example.hunain.emergencyapplication.Data_Access;

/**
 * Created by hunain on 4/7/2018.
 */

public interface IDataAccess<T> {
    void insert(T entity);
    void update(T entity);

}
