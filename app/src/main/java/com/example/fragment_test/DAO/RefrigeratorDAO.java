package com.example.fragment_test.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fragment_test.pojo.RefrigeratorIngredient;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface RefrigeratorDAO {
    @Query("SELECT id, name, img, sort, quantity, savingDay, expiration  FROM refrigerator" +
            " WHERE expired = 0")
    List<RefrigeratorIngredient> getAllRefrigeratorIngredients();

    @Query("INSERT INTO refrigerator (name, img, sort, quantity, savingDay, expiration) values ('牛排', '牛排照片', '肉類', '2', '7', '2024-06-30')")
    void addIngredients();
}
