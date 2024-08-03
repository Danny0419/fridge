package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fragment_test.entity.RecipeIngredient;

import java.util.List;

@Dao
public interface RecipeIngredientDAO {
    @Query("""
            SELECT id, name, quantity, img, r_id
            FROM recipe_needs
            WHERE r_id = :id
            """)
    List<RecipeIngredient> queryRecipeIngredientsByRId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRecipeIngredient(RecipeIngredient recipeIngredient);
}
