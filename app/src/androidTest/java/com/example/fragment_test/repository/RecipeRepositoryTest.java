package com.example.fragment_test.repository;

import androidx.room.ExperimentalRoomApi;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RecipeDAO;
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
public class RecipeRepositoryTest extends TestCase {

    public RecipeRepository recipeRepository;
    public RecipeDAO recipeDAO;

    @Test
    public void showRecipeCollectionRecipeListShouldEqualFour() {
        List<Recipe> recipes = List.of(
                new Recipe(0, "卡拉雞腿堡", "卡拉雞腿堡照片", 1, 0, 1, null),
                new Recipe(0, "荷包蛋", "卡拉雞腿堡照片", 1, 0, 1, null),
                new Recipe(0, "番茄炒蛋", "卡拉雞腿堡照片", 1, 0, 1, null),
                new Recipe(0, "清蒸鱸魚", "卡拉雞腿堡照片", 1, 0, 1, null)
        );
        recipes.forEach(recipe -> recipeRepository.collectRecipe(recipe));

        List<Recipe> collection = recipeRepository.showRecipeCollection();

        assertEquals(4, collection.size());
    }

    @Test
    public void collectOneRecipeRecipeTabAlreadyExist() {
        Recipe recipe = new Recipe(0, "卡拉雞腿堡", "卡拉雞腿堡照片", 1, 0, 0, null);
        recipeDAO.insertRecipe(recipe);
        Recipe collectingRecipe = new Recipe(0, "卡拉雞腿堡", "卡拉雞腿堡照片", 1, 0, 1, null);
        recipeRepository.collectRecipe(collectingRecipe);
        Recipe recipe1 = recipeDAO.queryRecipeByName("卡拉雞腿堡");
        assertEquals(1, recipe1.id);
        assertEquals("卡拉雞腿堡", recipe1.name);
        assertEquals(1, recipe1.collected);
    }

    @Test
    public void collectOneRecipeRecipeTabDoseNotExist() {
        Recipe recipe = new Recipe(0, "卡拉雞腿堡", "卡拉雞腿堡照片", 1, 0, 1, null);
        recipeRepository.collectRecipe(recipe);
        Recipe recipe1 = recipeDAO.queryRecipeByName("卡拉雞腿堡");
        assertEquals("卡拉雞腿堡", recipe1.name);
        assertEquals(1, recipe1.collected);
    }

    @Before
    public void setUp() {
        recipeRepository = RecipeRepository.getInstance(ApplicationProvider.getApplicationContext());
        recipeDAO = FridgeDatabase.getInstance(ApplicationProvider.getApplicationContext()).recipeDAO();
    }

    @After
    public void close() {
    }
}