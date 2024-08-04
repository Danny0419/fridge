package com.example.fragment_test.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "recipe", foreignKeys = @ForeignKey(entity = Schedule.class, parentColumns = "id", childColumns = "s_id", onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE))
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String img;
    @ColumnInfo
    public int serving;
    @ColumnInfo
    public int status;
    @ColumnInfo(name = "s_id")
    public Integer sId;

    @Ignore
    public List<RecipeIngredient> ingredients = new ArrayList<>();

    public Recipe(int id, String name, String img, int serving, int status, Integer sId) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.serving = serving;
        this.status = status;
        this.sId = sId;
    }

    public Recipe(int id, String name, String img, int serving, int status, Integer sId, RecipeIngredient ...ingredients) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.serving = serving;
        this.status = status;
        this.sId = sId;
        this.ingredients.addAll(Arrays.asList(ingredients));
    }


}
