package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IngredientUsageDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertIngredientUsage(IngredientUsage ingredientUsage);

    @Query(
            """
            SELECT *
            FROM ingredients_usage
            WHERE available > 0
            """
    )
    List<IngredientUsage> queryAvailableIngredients();
}
