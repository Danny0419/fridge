package com.example.fragment_test.ServerAPI;

import java.util.List;

public class Recipe {
    private int recipe_id;
    private String recipe_name;
    private List<Ingredient> ingredients;
    private int cooking_time;
    private String image;
    private int serving;

    // Getter and Setter methods
    public int getRecipe_id() {return recipe_id;}

    public void setRecipe_id(int recipe_id) {this.recipe_id = recipe_id;}

    public String getRecipe_name() {return recipe_name;}

    public void setRecipe_name(String recipe_name) {this.recipe_name = recipe_name;}

    public List<Ingredient> getIngredients() {return ingredients;}

    public void setIngredients(List<Ingredient> ingredients) {this.ingredients = ingredients;}

    public static class Ingredient {
        private String ingredient_name;
        private String ingredient_need;
        private String ingredient_category;


        public String getIngredient_name() {return ingredient_name;}

        public void setIngredient_name(String ingredient_name) {this.ingredient_name = ingredient_name;}

        public String getIngredient_need() {return ingredient_need;}

        public void setIngredient_need(String ingredient_need) {this.ingredient_need = ingredient_need;}

        public String getIngredient_category() {
            return ingredient_category;
        }

        public void setIngredient_category(String ingredient_category) {
            this.ingredient_category = ingredient_category;
        }

        // Getter and Setter methods
    }

    public int getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(int cooking_time) {
        this.cooking_time = cooking_time;
    }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public int getServing() { return serving; }

    public void setServing(int serving) { this.serving = serving; }
}
