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
            SELECT id, name, img, sort, quantity, expiration  FROM refrigerator
            """)
    List<RefrigeratorIngredient> getAllRefrigeratorIngredients();

    @Query("""
            SELECT id, name, img, sort, quantity, expiration  FROM refrigerator
            WHERE quantity > 0 AND expiration >= :today""")
    List<RefrigeratorIngredient> getQuantityGreaterZeroAndNotExpiredIngredients(int today);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertIngredient(RefrigeratorIngredient ingredient);

    @Insert
    long[] insertIngredients(List<RefrigeratorIngredient> ingredients);
}
