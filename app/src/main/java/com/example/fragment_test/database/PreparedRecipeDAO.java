package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fragment_test.entity.PreparedRecipe;

import java.util.List;

@Dao
public interface PreparedRecipeDAO {

    @Insert
    long insertPreparedRecipe(PreparedRecipe preparedRecipe);

    @Query("""
            SELECT id, r_id
            FROM preparedRecipes
            """)
    List<PreparedRecipe> queryAllPreparedRecipes();
}
