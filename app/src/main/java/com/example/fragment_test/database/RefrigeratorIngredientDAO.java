package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.vo.RefrigeratorIngredientDetailVO;
import com.example.fragment_test.vo.RefrigeratorIngredientVO;

import java.util.List;

@Dao
public interface RefrigeratorIngredientDAO {
    @Query("""
            SELECT id, name, img, sort, quantity, expiration  FROM refrigerator
            """)
    List<RefrigeratorIngredient> getAllRefrigeratorIngredients();

    @Query("""
            SELECT name, sort, img, min(expiration) as earlyEx, max(expiration) as lastEx, sum(quantity) as sumQuantity, unit
            FROM refrigerator
            WHERE quantity > 0 AND expiration >= :today
            GROUP BY name
            """)
    List<RefrigeratorIngredientVO> getQuantityGreaterZeroAndNotExpiredIngredientsOverallInfo(int today);

    @Query("""
            SELECT id, name, img, sort, quantity, purchase_date, expiration, unit
            FROM refrigerator
            WHERE quantity > 0 AND expiration >= :today
            """)
    List<RefrigeratorIngredient> getQuantityGreaterZeroAndNotExpiredIngredients(int today);

    @Query(
            """
            SELECT id, name, img, sort, quantity, purchase_date, expiration, unit
            FROM refrigerator
            WHERE name = :name AND expiration >= :today
            """
    )
    List<RefrigeratorIngredient> getIngredientsByName(String name, int today);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertIngredient(RefrigeratorIngredient ingredient);

    @Insert
    long[] insertIngredients(List<RefrigeratorIngredient> ingredients);

    @Update
    void updateRefrigeratorIngredientQuantity(RefrigeratorIngredient ingredient);

    @Query(
            """
            SELECT name, sum(quantity) as quantity, purchase_date as purchaseDate, expiration, expiration - :today as daysRemaining
            FROM refrigerator
            WHERE expiration >= :today
            GROUP BY name, purchaseDate, expiration
            HAVING sum(quantity) > 0 AND daysRemaining <= 3
            """
    )
    List<RefrigeratorIngredientDetailVO> getExpirationDaysLesserThanThreeDaysIngredients(int today);

    @Query(
            """
            SELECT *
            FROM refrigerator
            WHERE name = :name AND purchase_date = :purchaseDate AND expiration = :expiration
            """
    )
    RefrigeratorIngredient getIngredientByName(String name, int purchaseDate, int expiration);
}
