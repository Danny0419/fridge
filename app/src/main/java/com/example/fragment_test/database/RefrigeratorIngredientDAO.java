package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fragment_test.entity.RefrigeratorIngredient;

import java.util.List;

@Dao
public interface RefrigeratorIngredientDAO {
    @Query("""
            SELECT id, name, img, sort, quantity, saving_day, expiration, expired  FROM refrigerator
            WHERE expired = 0;
            """)
    List<RefrigeratorIngredient> getAllRefrigeratorIngredients();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertIngredient(RefrigeratorIngredient ingredient);

    @Insert
    long[] insertIngredients(List<RefrigeratorIngredient> ingredients);
}
