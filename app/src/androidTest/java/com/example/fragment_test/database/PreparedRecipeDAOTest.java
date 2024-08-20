package com.example.fragment_test.database;

import androidx.room.ExperimentalRoomApi;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

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
public class PreparedRecipeDAOTest extends TestCase {
    public FridgeDatabase database;
    public PreparedRecipeDAO preparedRecipeDAO;
    public RecipeDAO recipeDAO;

    @Test
    public void queryAllPreparedRecipesReturnListShouldEqualFour(){
        Recipe recipe = new Recipe(1, "炒蛋", "炒蛋照片", 2, 0);
        Recipe recipe1 = new Recipe(2, "炒麵", "炒麵照片", 2, 0);
        recipeDAO.insertRecipe(recipe);
        recipeDAO.insertRecipe(recipe1);

        List<PreparedRecipe> preparedRecipes = List.of(
                new PreparedRecipe(0, 1),
                new PreparedRecipe(0, 1),
                new PreparedRecipe(0, 2),
                new PreparedRecipe(0, 2)
        );
        preparedRecipes.forEach(preparedRecipe -> preparedRecipeDAO.insertPreparedRecipe(preparedRecipe));

        List<PreparedRecipe> preparedRecipeList = preparedRecipeDAO.queryAllPreparedRecipes();
        assertEquals(4, preparedRecipeList.size());
        assertEquals(1, preparedRecipeList.get(0).rId.intValue());
        assertEquals(1, preparedRecipeList.get(1).rId.intValue());
        assertEquals(2, preparedRecipeList.get(2).rId.intValue());
        assertEquals(2, preparedRecipeList.get(3).rId.intValue());
    }

    @Test
    public void insertOnePreparedRecipeRecipeIdIsOne(){
        Recipe recipe = new Recipe(1, "炒蛋", "炒蛋照片", 2, 0);
        recipeDAO.insertRecipe(recipe);
        PreparedRecipe preparedRecipe = new PreparedRecipe(0, 1);
        long l = preparedRecipeDAO.insertPreparedRecipe(preparedRecipe);
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
        preparedRecipeDAO = database.preparedRecipeDAO();
    }

    @After
    public void close() {
        database.close();
    }
}