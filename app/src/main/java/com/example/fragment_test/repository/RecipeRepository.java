package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RecipeDAO;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeRepository {
    private static RecipeRepository recipeRepository;
    private final RecipeDAO recipeDAO;
    private final RecipeIngredientRepository recipeIngredientRepository;

    private RecipeRepository(Context context) {
        this.recipeDAO = FridgeDatabase.getInstance(context).recipeDAO();
        this.recipeIngredientRepository = RecipeIngredientRepository.getInstance(context);
    }

    public static RecipeRepository getInstance(Context context) {
        if (recipeRepository == null) {
            recipeRepository = new RecipeRepository(context);
        }
        return recipeRepository;
    }

    public Optional<List<Recipe>> recommendRecipes() {
        return Optional.of(List.of(
                new Recipe(0, "蔬菜大餐", "照片", 2, 0, 0, null),
                new Recipe(0, "菲力牛排", "照片", 2, 0, 0, null),
                new Recipe(0, "義大利麵", "照片", 2, 0, 0, null),
                new Recipe(0, "蛋包飯", "照片", 2, 0, 0, null),
                new Recipe(0, "卡拉雞腿堡", "照片", 2, 0, 0, null),
                new Recipe(0, "聖誕大餐", "照片", 2, 0, 0, null),
                new Recipe(0, "來一客", "照片", 2, 0, 0, null)
        ));
    }

    public void collectRecipe(Recipe recipe) {
        Recipe queryRecipeByName = recipeDAO.queryRecipeByName(recipe.name);
        if (queryRecipeByName != null) {
            recipe.id = queryRecipeByName.id;
        }
        recipeDAO.insertRecipe(recipe);
    }
}