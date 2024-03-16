package com.example.fragment_test.pojo;

import android.graphics.drawable.Drawable;

public class Recipe {
    private String foodImg;
    private String foodName;

    public Recipe() {
    }

    public Recipe(String foodImg, String foodName) {
        this.foodImg = foodImg;
        this.foodName = foodName;
    }

    public String getFoodImg() {
        return foodImg;
    }

    public String getFoodName() {
        return foodName;
    }
}
