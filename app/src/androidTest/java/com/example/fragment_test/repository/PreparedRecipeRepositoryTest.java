package com.example.fragment_test.repository;

import androidx.room.ExperimentalRoomApi;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.PreparedRecipeDAO;
import com.example.fragment_test.database.RecipeDAO;
import com.example.fragment_test.entity.PreparedRecipe;
import com.example.fragment_test.entity.Recipe;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@ExperimentalRoomApi
@RunWith(AndroidJUnit4.class)
@SmallTest
public class PreparedRecipeRepositoryTest extends TestCase {
    public FridgeDatabase database;
    public PreparedRecipeRepository preparedRecipeRepository;
    public RecipeDAO recipeDAO;
    public PreparedRecipeDAO preparedRecipeDAO;

    @Test
    public void addOneInterestingRecipe(){
        Recipe recipe = new Recipe(1, "炒蛋", "炒蛋照片", 2, 0);
        preparedRecipeRepository.addInterestingRecipe(recipe);
        Recipe recipe1 = recipeDAO.queryRecipeById(1);
        assertEquals(1, recipe1.id);
        List<PreparedRecipe> preparedRecipes = preparedRecipeDAO.queryAllPreparedRecipes();
        assertEquals(1, preparedRecipes.size());
        assertEquals(1, preparedRecipes.get(0).rId.intValue());
    }

    @Before
    public void setUp() {
        preparedRecipeRepository = PreparedRecipeRepository.getInstance(ApplicationProvider.getApplicationContext());
        recipeDAO = FridgeDatabase.getInstance(ApplicationProvider.getApplicationContext()).recipeDAO();
        preparedRecipeDAO = FridgeDatabase.getInstance(ApplicationProvider.getApplicationContext()).preparedRecipeDAO();
    }

    @After
    public void close() {
    }

}