package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RecipeIngredientDAO;
import com.example.fragment_test.entity.RecipeIngredient;
import com.example.fragment_test.entity.ScheduleRecipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientRepository {
    private static RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeIngredientDAO recipeIngredientDAO;

    private RecipeIngredientRepository(Context context) {
        this.recipeIngredientDAO = FridgeDatabase.getInstance(context).recipeIngredientDAO();
    }

    public static RecipeIngredientRepository getInstance(Context context) {
        if (recipeIngredientRepository == null) {
            recipeIngredientRepository = new RecipeIngredientRepository(context);
        }
        return recipeIngredientRepository;
    }

    public List<RecipeIngredient> getRecipesUsingIngredient(List<ScheduleRecipe> scheduleRecipes) {
        List<RecipeIngredient> recipesUsingIngredients = new ArrayList<>();
        scheduleRecipes.forEach(scheduleRecipe -> recipesUsingIngredients.addAll(recipeIngredientDAO.queryRecipeIngredientsByRId(scheduleRecipe.rid)));
        return recipesUsingIngredients;
    }
}
