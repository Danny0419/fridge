package com.example.fragment_test.service;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ShoppingService {
    @GET("api/all_ingredients_sorts")
    Single<List<String>> getAllSortsOfIngredients();

    @GET("api/sort_of_ingredients_name/{sort}")
    Single<List<String>> getSortOfIngredientsName(
            @Path("sort") String sort
    );
}
