package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.PreparedRecipeDAO;
import com.example.fragment_test.entity.PreparedRecipe;
import com.example.fragment_test.entity.Recipe;

public class PreparedRecipeRepository {
    private static PreparedRecipeRepository preparedRecipeRepository;
    private final PreparedRecipeDAO preparedRecipeDAO;
    private final RecipeRepository recipeRepository;
    private PreparedRecipeRepository(Context context) {
        this.preparedRecipeDAO = FridgeDatabase.getInstance(context).preparedRecipeDAO();
        this.recipeRepository = RecipeRepository.getInstance(context);
    }

    public static PreparedRecipeRepository getInstance(Context context) {
        if (preparedRecipeRepository == null) {
            preparedRecipeRepository = new PreparedRecipeRepository(context);
        }
        return preparedRecipeRepository;
    }

    public void addInterestingRecipe(Recipe recipe) {
        recipeRepository.storeRecipe(recipe);
        PreparedRecipe preparedRecipe = new PreparedRecipe(0, recipe.id);
        preparedRecipeDAO.insertPreparedRecipe(preparedRecipe);
    }

}
