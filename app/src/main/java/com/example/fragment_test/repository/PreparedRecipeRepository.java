package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.PreparedRecipeDAO;
import com.example.fragment_test.entity.PreparedRecipe;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeWithPreRecipeId;

import java.util.List;

public class PreparedRecipeRepository {
    private static PreparedRecipeRepository preparedRecipeRepository;
    private final PreparedRecipeDAO preparedRecipeDAO;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private PreparedRecipeRepository(Context context) {
        this.preparedRecipeDAO = FridgeDatabase.getInstance(context).preparedRecipeDAO();
        this.recipeRepository = RecipeRepository.getInstance(context);
        this.recipeIngredientRepository = RecipeIngredientRepository.getInstance(context);
    }

    public static PreparedRecipeRepository getInstance(Context context) {
        if (preparedRecipeRepository == null) {
            preparedRecipeRepository = new PreparedRecipeRepository(context);
        }
        return preparedRecipeRepository;
    }

    public void addInterestingRecipe(Recipe recipe) {
        recipeRepository.storeRecipe(recipe);
        recipeIngredientRepository.addRecipeIngredients(recipe.ingredients);
        PreparedRecipe preparedRecipe = new PreparedRecipe(0, recipe.id, null, 0);
        preparedRecipeDAO.insertPreparedRecipe(preparedRecipe);
    }

    public void schedule(RecipeWithPreRecipeId recipeWithPreRecipeId) {
        PreparedRecipe preparedRecipe = new PreparedRecipe(recipeWithPreRecipeId.pRId, recipeWithPreRecipeId.id, 1);
        preparedRecipeDAO.insertPreparedRecipe(preparedRecipe);
    }

    public List<RecipeWithPreRecipeId> getPreparedRecipes() {
        return preparedRecipeDAO.queryAllRecipes();
    }

    public void unSchedule(PreparedRecipe preparedRecipe) {
        preparedRecipeDAO.insertPreparedRecipe(preparedRecipe);
    }
}
