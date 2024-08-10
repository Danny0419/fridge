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

    @Test
    public void queryNameIs荷包蛋Recipe(){
        Recipe recipe = new Recipe(0, "荷包蛋", "荷包蛋照片", 2);
        recipeDAO.insertRecipe(recipe);

        Recipe recipe1 = recipeDAO.queryRecipeByName("荷包蛋");
        assertEquals("荷包蛋", recipe1.name);
    }


    @Test
    public void insertOneRecipeRowIdShouldEqualOne() {
        Recipe recipe = new Recipe(0, "荷包蛋", "荷包蛋照片", 2);
        long l = recipeDAO.insertRecipe(recipe);
        assertEquals(1, l);
    }

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
}