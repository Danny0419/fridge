package com.example.fragment_test.service;

import com.example.fragment_test.ServerAPI.Recipe;
import com.example.fragment_test.entity.Step;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecipeService {
    @GET("api/get_recipe")
    Call<List<Recipe>> getRecipes();

    @GET("api/get_recipe_steps/{recipe_id}")
    Single<List<Step>> getRecipeSteps(
            @Path("recipe_id") int recipeId
    );
}
