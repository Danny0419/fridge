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
            select id, name, img, serving, s_id
            from recipe
            where s_id = :sId""")
    List<Recipe> getRecipeById(int sId);
}
