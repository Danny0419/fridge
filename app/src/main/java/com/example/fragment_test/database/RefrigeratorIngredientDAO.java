package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.entity.RefrigeratorIngredientVO;

import java.util.List;

@Dao
public interface RefrigeratorIngredientDAO {
    @Query("""
            SELECT id, name, img, sort, quantity, expiration  FROM refrigerator
            """)
    List<RefrigeratorIngredient> getAllRefrigeratorIngredients();

    @Query("""
            SELECT name, sort, img, min(expiration) as earlyEx, max(expiration) as lastEx, sum(quantity) as sumQuantity
            FROM refrigerator
            WHERE quantity > 0 AND expiration >= :today
            GROUP BY name
            """)
    List<RefrigeratorIngredientVO> getQuantityGreaterZeroAndNotExpiredIngredientsOverallInfo(int today);

    @Query("""
            SELECT id, name, img, sort, quantity, expiration  FROM refrigerator
            WHERE quantity > 0 AND expiration >= :today""")
    List<RefrigeratorIngredient> getQuantityGreaterZeroAndNotExpiredIngredients(int today);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertIngredient(RefrigeratorIngredient ingredient);

    @Insert
    long[] insertIngredients(List<RefrigeratorIngredient> ingredients);
}
