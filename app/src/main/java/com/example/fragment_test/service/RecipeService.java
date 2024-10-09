package com.example.fragment_test.service;

import com.example.fragment_test.ServerAPI.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {
    @GET("api/get_recipe")
    Call<List<Recipe>> getRecipes();
}
