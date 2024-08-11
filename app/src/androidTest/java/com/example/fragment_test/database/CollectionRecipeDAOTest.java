package com.example.fragment_test.database;

import androidx.room.ExperimentalRoomApi;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.example.fragment_test.entity.CollectionRecipe;
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
public class CollectionRecipeDAOTest extends TestCase {
    public FridgeDatabase database;
    public CollectionRecipeDAO collectionRecipeDAO;
    public RecipeDAO recipeDAO;

    @Test
    public void queryAllCollectionRecipesReturnListShouldEqualFour() {
        Recipe recipe = new Recipe(1, "炒蛋", "炒蛋照片", 2);
        Recipe recipe1 = new Recipe(2, "炒麵", "炒麵照片", 2);
        recipeDAO.insertRecipe(recipe);
        recipeDAO.insertRecipe(recipe1);

        List<CollectionRecipe> collectionRecipes = List.of(
                new CollectionRecipe(1),
                new CollectionRecipe(2)
        );
        collectionRecipes.forEach(collectionRecipe -> collectionRecipeDAO.insertPreparedRecipe(collectionRecipe));

        collectionRecipes = collectionRecipeDAO.queryAllPreparedRecipes();
        assertEquals(2, collectionRecipes.size());
        assertEquals(1, collectionRecipes.get(0).rId.intValue());
        assertEquals(2, collectionRecipes.get(1).rId.intValue());
    }


    @Test
    public void insertOneCollectionRecipeRecipeIdIsOne() {
        Recipe recipe = new Recipe(1, "炒蛋", "炒蛋照片", 2);
        recipeDAO.insertRecipe(recipe);
        CollectionRecipe collectionRecipe = new CollectionRecipe(1);
        long l = collectionRecipeDAO.insertPreparedRecipe(collectionRecipe);
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
        collectionRecipeDAO = database.collectionRecipeDAO();
    }

    @After
    public void close() {
        database.close();
    }
}