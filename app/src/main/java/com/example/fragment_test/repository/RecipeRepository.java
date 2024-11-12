package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.RecipeRecommend.RecipeRecommendation;
import com.example.fragment_test.ServerAPI.RetrofitClient;
import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RecipeDAO;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeIngredient;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.entity.Step;
import com.example.fragment_test.service.RecipeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecipeRepository {
    private static RecipeRepository recipeRepository;
    private final RecipeDAO recipeDAO;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RefrigeratorIngredientRepository refrigeratorIngredientRepository;
    private RecipeService recipeService;
    private final RecipeRecommendation recipeRecommendation;

    private RecipeRepository(Context context) {
        this.recipeDAO = FridgeDatabase.getInstance(context).recipeDAO();
        this.recipeIngredientRepository = RecipeIngredientRepository.getInstance(context);
        this.refrigeratorIngredientRepository = RefrigeratorIngredientRepository.getInstance(context);
        this.recipeRecommendation = new RecipeRecommendation();
        this.recipeService = RetrofitClient.getRetrofitInstance().create(RecipeService.class);
    }

    public static RecipeRepository getInstance(Context context) {
        if (recipeRepository == null) {
            recipeRepository = new RecipeRepository(context);
        }
        return recipeRepository;
    }

    public Optional<List<Recipe>> recommendRecipes() {

        List<RefrigeratorIngredient> refrigeratorIngredients = refrigeratorIngredientRepository.getRefrigeratorIngredients();
        Map<String, RecipeRecommendation.FridgeIngredient> ingredientMap = refrigeratorIngredients.stream()
                .collect(Collectors.toMap(o -> o.name, o -> new RecipeRecommendation.FridgeIngredient(o.name, o.quantity, o.expiration), (o1, o2) -> {
                    o1.setQuantity(o1.getQuantity() + o2.getQuantity());
                    if (o1.getExpiryDays() > o2.getExpiryDays()) {
                        o1.setExpiryDays(o2.getExpiryDays());
                    }
                    return o1;
                }));
        List<RecipeRecommendation.Recommendation> recommendations = recipeRecommendation.getRecommendations(ingredientMap);
        List<Recipe> recipes = recommendations.stream()
                .map(o -> {
                    com.example.fragment_test.ServerAPI.Recipe recipe = o.getRecipe();
                    Recipe r = new Recipe(recipe.getRecipe_id()
                            , recipe.getRecipe_name()
                            , recipe.getImage()
                            , recipe.getServing()
                    );
                    List<RecipeIngredient> ingredients = new ArrayList<>();
                    recipe.getIngredients()
                            .forEach(o1 -> ingredients.add(new RecipeIngredient(o1.getIngredient_name(), Integer.parseInt(o1.getIngredient_need()))));
                    r.setIngredients(ingredients);
                    return r;
                })
                .collect(Collectors.toList());
        return Optional.of(recipes);
    }

    public long storeRecipe(Recipe recipe) {
        return recipeDAO.insertRecipe(recipe);
    }

    public void collectRecipe(Recipe recipe) {
        recipe.collected = 1;
        recipeDAO.insertRecipe(recipe);
    }

    public void unCollectRecipe(Recipe recipe) {
        recipe.collected = 0;
        recipeDAO.insertRecipe(recipe);
    }

    public List<Recipe> showRecipeCollection() {
        return recipeDAO.queryAllCollectedRecipe();
    }

    public List<RecipeIngredient> getRecipeIngredients(int id) {
        return recipeIngredientRepository.getRecipeIngredientsByRId(id);
    }

    public List<Step> getRecipeSteps(Recipe recipe) {
        return recipeService.getRecipeSteps(recipe.id);
    }
}
