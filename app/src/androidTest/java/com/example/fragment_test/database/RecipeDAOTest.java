package com.example.fragment_test.database;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fragment_test.entity.Recipe;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class RecipeDAOTest extends TestCase {


    public FridgeDatabase database;
    public RecipeDAO recipeDAO;

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        FridgeDatabase.class
                ).allowMainThreadQueries()
                .build();
        recipeDAO = database.recipeDAO();
    }

    @After
    public void tearDown() throws Exception {
        database.close();
    }

    @Test
    public void insertOneRecipeRowIdShouldEqualOne() {
        Recipe recipe = new Recipe(0, "荷包蛋", "荷包蛋照片",0, 1, 1);
        long l = recipeDAO.insertRecipe(recipe);
        assertEquals(1, l);
    }

    @Test
    public void getSidEqualOneRecipesSizeShouldEqualThree() {

        List<Recipe> recipes = List.of(
                new Recipe(0, "荷包蛋", "荷包蛋照片",2, 0, 1),
                new Recipe(0, "荷包蛋", "荷包蛋照片", 2, 0, 1),
                new Recipe(0, "荷包蛋", "荷包蛋照片", 3, 0, 1)
        );
        recipes.forEach((recipe -> recipeDAO.insertRecipe(recipe)));
        List<Recipe> recipesList = recipeDAO.queryRecipeByScheduleId(1);
        assertEquals(3, recipesList.size());

    }

    @Test
    public void getSidEqualOneRecipesSizeShouldEqualTwo() {

        List<Recipe> recipes = List.of(
                new Recipe(0, "荷包蛋", "荷包蛋照片", 1,0 , 1),
                new Recipe(0, "荷包蛋", "荷包蛋照片", 2, 0, 2),
                new Recipe(0, "荷包蛋", "荷包蛋照片", 3, 0, 1)
        );
        recipes.forEach((recipe -> recipeDAO.insertRecipe(recipe)));
        List<Recipe> recipesList = recipeDAO.queryRecipeByScheduleId(1);
        assertEquals(2, recipesList.size());

    }

    @Test
    public void updateRecipeStatusToOne() {
        List<Recipe> recipes = List.of(
                new Recipe(0, "荷包蛋", "荷包蛋照片", 1, 0,  1),
                new Recipe(0, "荷包蛋", "荷包蛋照片", 2, 0 , 2),
                new Recipe(0, "荷包蛋", "荷包蛋照片", 3, 0, 1)
        );

        recipes.forEach((recipe) -> recipeDAO.insertRecipe(recipe));
        recipeDAO.updateRecipeStatus(1);
        List<Recipe> recipeByScheduleId = recipeDAO.queryRecipeByScheduleId(1);
        assertEquals(1, recipeByScheduleId.size());

    }
}