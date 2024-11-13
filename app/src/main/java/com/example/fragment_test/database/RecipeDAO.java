package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fragment_test.entity.Recipe;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRecipe(Recipe recipe);

    @Query("""
            SELECT id, name, src, serving, collected
            FROM recipes
            WHERE name = :name
            """)
    Recipe queryRecipeByName(String name);

    @Query("""
            SELECT id, name, src, serving, collected
            FROM recipes
            WHERE id = :id
            """)
    Recipe queryRecipeById(Integer id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(Recipe recipe);

    @Query("""
            SELECT id, name, src, serving, collected
            FROM recipes
            WHERE collected = 1
            """)
    List<Recipe> queryAllCollectedRecipe();
}
