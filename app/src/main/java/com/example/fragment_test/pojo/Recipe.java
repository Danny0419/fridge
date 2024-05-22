package com.example.fragment_test.pojo;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Recipe {
    public String foodImg;
    public String foodName;
    public ArrayList<Ingredient> ingredients;

    public Recipe() {
    }

    public Recipe(String foodImg, String foodName, ArrayList<Ingredient> ingredients) {
        this.foodImg = foodImg;
        this.foodName = foodName;
        this.ingredients = ingredients;
    }
}
