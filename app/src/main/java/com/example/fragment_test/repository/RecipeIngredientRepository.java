package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RecipeIngredientDAO;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeIngredient;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    public long saveRecipeIngredient(List<RecipeIngredient> ingredients) {
        AtomicInteger successful = new AtomicInteger();
        ingredients.forEach(ingredient -> {
            if (recipeIngredientDAO.insertRecipeIngredient(ingredient) != 0) {
                successful.getAndIncrement();
            }
        });
        return successful.longValue();
    }

    public List<RecipeIngredient> getRecipeNeedsIngredients(Recipe recipe) {
        return recipeIngredientDAO.queryRecipeIngredientsByRId(recipe.id);
    }
}
