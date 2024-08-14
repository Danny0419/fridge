package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RecipeDAO;
import com.example.fragment_test.entity.Recipe;

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
                new Recipe(1, "蔬菜大餐", "照片", 2, 0),
                new Recipe(2, "菲力牛排", "照片", 2, 1),
                new Recipe(3, "義大利麵", "照片", 2, 0),
                new Recipe(4, "蛋包飯", "照片", 2, 0),
                new Recipe(5, "卡拉雞腿堡", "照片", 2, 0),
                new Recipe(6, "聖誕大餐", "照片", 2, 1),
                new Recipe(7, "來一客", "照片", 2, 1)
        ));
    }

    public void collectRecipe(Recipe recipe) {
        Recipe queryRecipeByName = recipeDAO.queryRecipeByName(recipe.name);
        if (queryRecipeByName != null) {
            recipe.id = queryRecipeByName.id;
        }
        recipeDAO.insertRecipe(recipe);
    }

    public List<Recipe> showRecipeCollection() {
        return null;
    }

    public long storeRecipe(Recipe recipe) {
        return recipeDAO.insertRecipe(recipe);
    }
}
