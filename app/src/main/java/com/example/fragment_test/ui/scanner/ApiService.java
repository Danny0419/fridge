package com.example.fragment_test.ui.scanner;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.List;

// 定義 API 介面
public interface ApiService {
    @GET("api/combined_ingredients")
    Call<List<CombinedIngredient>> getCombinedIngredients(@Query("product_name") String productName);
}
