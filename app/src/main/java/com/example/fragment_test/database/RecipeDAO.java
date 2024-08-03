package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fragment_test.entity.Recipe;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Insert
    long insertRecipe(Recipe recipe);

    @Query("""
            select id, name, img, serving, status, s_id
            from recipe
            where s_id = :sId AND status = 0
            """)
    List<Recipe> queryRecipeByScheduleId(Integer sId);

    @Query("""
            SELECT id, name, img, serving, status, s_id
            FROM recipe
            WHERE id = :id
            """)
    Recipe queryRecipeById(Integer id);
    @Query("""
            UPDATE recipe set status = 1
            WHERE id = :id
            """)
    void updateRecipeStatus(int id);
}
