package com.example.fragment_test.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "recipe")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String img;
    @ColumnInfo
    public int serving;

    @Ignore
    public List<Step> steps;
    @Ignore
    public List<RecipeIngredient> ingredients;

    public Recipe(int id, String name, String img, int serving) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.serving = serving;
        steps = new ArrayList<>();
        ingredients = new ArrayList<>();
    }

    public Recipe(int id, String name, String img, int serving, int collected, Integer sId, List<Step> steps, List<RecipeIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.serving = serving;
        this.steps = steps;
        this.ingredients = ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}
