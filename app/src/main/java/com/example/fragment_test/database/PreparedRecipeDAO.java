package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fragment_test.entity.PreparedRecipe;
import com.example.fragment_test.entity.RecipeWithPreRecipeId;

import java.util.List;

@Dao
public interface PreparedRecipeDAO {

    @Query("""
            SELECT id, r_id, s_r_id, scheduled
            FROM prepared_recipes
            """)
    List<PreparedRecipe> queryAllPreparedRecipes();

    @Query(
            """
            SELECT r.id as id, r.name as name, r.img as img, r.collected as collected, r.serving as serving, p_r.id as pRId
            FROM prepared_recipes p_r join recipes r
            on p_r.r_id = r.id
            """
    )
    List<RecipeWithPreRecipeId> queryAllRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPreparedRecipe(PreparedRecipe preparedRecipe);
}
