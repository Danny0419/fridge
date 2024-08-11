package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fragment_test.entity.CollectionRecipe;

import java.util.List;

@Dao
public interface CollectionRecipeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPreparedRecipe(CollectionRecipe collectionRecipe);

    @Query("""
            SELECT r_id
            FROM recipe_collection
            """)
    List<CollectionRecipe> queryAllPreparedRecipes();
}
