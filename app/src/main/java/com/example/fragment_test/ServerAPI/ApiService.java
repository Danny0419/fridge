package com.example.fragment_test.ServerAPI;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    // 调用 /test_connection 接口
    @GET("test_connection")
    Call<ServerResponse> testConnection();

//    // 调用 /api/ingredients 接口
//    @GET("api/ingredients")
//    Call<List<Ingredient>> getIngredients(String ingredientName);

    @GET("api/get_recipe")
    Call<List<Recipe>> getRecipes();
//    Call<ResponseBody> getRecipes();

    @GET("api/all_ingredients_sorts")
    Single<List<String>> getAllSortsOfIngredients();


    // 调用 /api/combined_ingredients 接口，根據 product_name 查詢資料
    @GET("api/combined_ingredients")
    Call<List<CombinedIngredient>> getCombinedIngredients(@Query("product_name") String productName);

    // 其他 API 接口

}