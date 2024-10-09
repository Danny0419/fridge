package com.example.fragment_test.ServerAPI;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    // 调用 /test_connection 接口
    @GET("test_connection")
    Call<ServerResponse> testConnection();

    // 调用 /api/ingredients 接口
    @GET("api/ingredients")
    Call<List<Ingredient>> getIngredients();

    @GET("api/get_recipe")
    Call<List<Recipe>> getRecipes();

    @GET("api/all_ingredients_sorts")
    Single<List<String>> getAllSortsOfIngredients();


    // 其他 API 接口
}