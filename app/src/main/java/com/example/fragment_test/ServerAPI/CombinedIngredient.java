package com.example.fragment_test.ServerAPI;

public class CombinedIngredient {
    private int Supermarket_ingredient_ID;
    private int Ingredient_ID;
    private String supermarket_ingredientcol_name;
    private int Grams;
    private String Ingredient_Name;
    private String Ingredients_category;
    private int expiration;

    // Getters and setters
    public int getSupermarket_ingredient_ID() {
        return Supermarket_ingredient_ID;
    }

    public void setSupermarket_ingredient_ID(int supermarket_ingredient_ID) {
        Supermarket_ingredient_ID = supermarket_ingredient_ID;
    }

    public int getIngredient_ID() {
        return Ingredient_ID;
    }

    public void setIngredient_ID(int ingredient_ID) {
        Ingredient_ID = ingredient_ID;
    }

    public String getSupermarket_ingredientcol_name() {
        return supermarket_ingredientcol_name;
    }

    public void setSupermarket_ingredientcol_name(String supermarket_ingredientcol_name) {
        this.supermarket_ingredientcol_name = supermarket_ingredientcol_name;
    }

    public int getGrams() {
        return Grams;
    }

    public void setGrams(int grams) {
        Grams = grams;
    }

    public String getIngredient_Name() {
        return Ingredient_Name;
    }

    public void setIngredient_Name(String ingredient_Name) {
        Ingredient_Name = ingredient_Name;
    }

    public String getIngredients_category() {
        return Ingredients_category;
    }

    public void setIngredients_category(String ingredients_category) {
        Ingredients_category = ingredients_category;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }
}