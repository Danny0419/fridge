package com.example.fragment_test.repository;

import androidx.room.ExperimentalRoomApi;
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
public class RecipeRepositoryTest extends TestCase {

    public RecipeRepository recipeRepository;

    @Test
    public void collectOneRecipe() {
        Recipe recipe = new Recipe(0, "卡拉雞腿堡", "卡拉雞腿堡照片", 1, 0, 0, null);
        recipeRepository.collectRecipe(recipe);

    }

    @Before
    public void setUp() {
        recipeRepository = RecipeRepository.getInstance(ApplicationProvider.getApplicationContext());
    }

    @After
    public void close() {
    }
}