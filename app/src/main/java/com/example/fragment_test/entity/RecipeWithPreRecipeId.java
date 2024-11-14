package com.example.fragment_test.entity;

import androidx.room.Embedded;

public class RecipeWithPreRecipeId {
    @Embedded
    public Recipe recipe;
    public int pRId;

    public RecipeWithPreRecipeId(Recipe recipe, int pRId) {
        this.recipe = recipe;
        this.pRId = pRId;
    }
}
