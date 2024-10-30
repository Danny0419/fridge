package com.example.fragment_test.ui.scanner;

public class CombinedIngredient {
    private int Supermarket_ingredient_ID;
    private int Ingredient_ID;
    private String supermarket_ingredientcol_name;
    private String unit;
    private String Grams;
    private String Ingredient_Name;
    private String Ingredients_category;
    private String expiration;

    // Getter 和 Setter 方法
    public int getSupermarket_ingredient_ID() {
        return Supermarket_ingredient_ID;
    }

    public void setSupermarket_ingredient_ID(int supermarket_ingredient_ID) {
        this.Supermarket_ingredient_ID = supermarket_ingredient_ID;
    }

    public int getIngredient_ID() {
        return Ingredient_ID;
    }

    public void setIngredient_ID(int ingredient_ID) {
        this.Ingredient_ID = ingredient_ID;
    }

    public String getSupermarket_ingredientcol_name() {
        return supermarket_ingredientcol_name;
    }

    public void setSupermarket_ingredientcol_name(String supermarket_ingredientcol_name) {
        this.supermarket_ingredientcol_name = supermarket_ingredientcol_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getGrams() {
        return Grams;
    }

    public void setGrams(String grams) {
        this.Grams = grams;
    }

    public String getIngredient_Name() {
        return Ingredient_Name;
    }

    public void setIngredient_Name(String ingredient_Name) {
        this.Ingredient_Name = ingredient_Name;
    }

    public String getIngredients_category() {
        return Ingredients_category;
    }

    public void setIngredients_category(String ingredients_category) {
        this.Ingredients_category = ingredients_category;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}
