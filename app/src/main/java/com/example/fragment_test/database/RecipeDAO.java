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
            SELECT id, name, img, serving, status, collected, s_id
            FROM recipe
            WHERE name = :name
            """)
    Recipe queryRecipeByName(String name);

    @Query("""
            select id, name, img, serving, status, collected, s_id
            from recipe
            where s_id = :sId AND status = 0
            """)
    List<Recipe> queryRecipeByScheduleId(Integer sId);

    @Query("""
            SELECT id, name, img, serving, status, collected, s_id
            FROM recipe
            WHERE id = :id
            """)
    Recipe queryRecipeById(Integer id);

    @Query("""
            UPDATE recipe set status = 1
            WHERE id = :id
            """)
    void updateRecipeStatus(int id);

    @Query( """
            SELECT id, name, img, serving, status, collected, s_id
            FROM recipe
            WHERE collected = 1
            """)
    List<Recipe> queryRecipeByIsCollected();
}
