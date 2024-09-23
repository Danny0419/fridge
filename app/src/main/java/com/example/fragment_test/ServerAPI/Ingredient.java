package com.example.fragment_test.ServerAPI;

public class Ingredient {
    private int Ingredient_ID;
    private String Ingredient_Name;
    private String Ingredient_category;
    private String expiration;

    // Getters and Setters
    public int getIngredient_ID() {
        return Ingredient_ID;
    }

    public void setIngredient_ID(int ingredient_ID) {
        Ingredient_ID = ingredient_ID;
    }

    public String getIngredient_Name() {
        return Ingredient_Name;
    }

    public void setIngredient_Name(String ingredient_Name) {
        Ingredient_Name = ingredient_Name;
    }

    public String getIngredient_category() {
        return Ingredient_category;
    }

    public void setIngredient_category(String ingredient_category) {
        Ingredient_category = ingredient_category;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}
