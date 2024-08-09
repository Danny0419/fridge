package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fragment_test.entity.Recipe;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRecipe(Recipe recipe);

    @Query("""
            SELECT id, name, img, serving
            FROM recipe
            WHERE name = :name
            """)
    Recipe queryRecipeByName(String name);

    @Query("""
            SELECT id, name, img, serving
            FROM recipe
            WHERE id = :id
            """)
    Recipe queryRecipeById(Integer id);

}
