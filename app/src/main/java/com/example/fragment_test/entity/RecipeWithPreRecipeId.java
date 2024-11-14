package com.example.fragment_test.entity;

public class RecipeWithPreRecipeId {
    public int id;
    public String name;
    public String img;
    public int serving;
    public int collected;
    public int pRId;

    public RecipeWithPreRecipeId(int id, String name, String img, int serving, int collected, int pRId) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.serving = serving;
        this.collected = collected;
        this.pRId = pRId;
    }
}
