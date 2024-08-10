package com.example.fragment_test.database;

import androidx.room.ExperimentalRoomApi;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeIngredient;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@ExperimentalRoomApi
@RunWith(AndroidJUnit4.class)
@SmallTest
public class RecipeIngredientDAOTest extends TestCase {
    public FridgeDatabase database;
    public RecipeDAO recipeDAO;
    public RecipeIngredientDAO recipeIngredientDAO;

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        FridgeDatabase.class
                ).allowMainThreadQueries()
                .build();
        recipeDAO = database.recipeDAO();
        recipeIngredientDAO = database.recipeIngredientDAO();
    }

    @After
    public void close() {
        database.close();
    }

    @Test
    public void insertOneRecipeIngredientRowIdShouldEqualOne() {
        Recipe recipe = new Recipe(0, "胡蘿蔔大餐", "胡蘿蔔大餐", 7);
        long l1 = recipeDAO.insertRecipe(recipe);
        assertEquals(1, l1);
        RecipeIngredient recipeIngredient = new RecipeIngredient(0, "胡蘿蔔", 3, "胡蘿蔔照片", 1);
        long l = recipeIngredientDAO.insertRecipeIngredient(recipeIngredient);
        assertEquals(1, l);
    }

    @Test
    public void queryRidIsOneRecipeIngredientsReturnedListSizeShouldEqualFour() {
        Recipe recipe = new Recipe(0, "胡蘿蔔大餐", "胡蘿蔔大餐", 7);
        long l1 = recipeDAO.insertRecipe(recipe);
        assertEquals(1, l1);

        List.of(
                new RecipeIngredient(0, "胡蘿蔔", 3, "胡蘿蔔照片", 1),
                new RecipeIngredient(0, "胡蘿蔔", 3, "胡蘿蔔照片", 1),
                new RecipeIngredient(0, "胡蘿蔔", 3, "胡蘿蔔照片", 1),
                new RecipeIngredient(0, "胡蘿蔔", 3, "胡蘿蔔照片", 1)
        ).forEach(recipeIngredient -> recipeIngredientDAO.insertRecipeIngredient(recipeIngredient));

        List<RecipeIngredient> recipeIngredients = recipeIngredientDAO.queryRecipeIngredientsByRId(1);
        assertEquals(4, recipeIngredients.size());
    }
}

