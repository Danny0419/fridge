package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.fragment_test.entity.RefrigeratorIngredient;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface RefrigeratorDAO {
    @Query("""
            SELECT id, name, img, sort, quantity, saving_day, expiration  FROM refrigerator
            WHERE expired = 0;
            """)
    List<RefrigeratorIngredient> getAllRefrigeratorIngredients();

    @Query("""
            INSERT INTO refrigerator (name, img, sort, quantity, saving_day, expiration) values ('牛排', '牛排照片', '肉類', '2', '7', '2024-06-30')
            """)
    void addIngredients();
}
