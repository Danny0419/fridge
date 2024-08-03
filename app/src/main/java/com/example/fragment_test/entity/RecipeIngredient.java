package com.example.fragment_test.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

@Entity(tableName = "recipe_needs",
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = "id", childColumns = "r_id",
                onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
public class RecipeIngredient extends Ingredient {
    @ColumnInfo
    public String img;
    @ColumnInfo(name = "r_id")
    public Integer rId;

    @Ignore
    public RecipeIngredient(@NonNull String name, Integer quantity, String img, Integer rId) {
        super(name, quantity);
        this.img = img;
        this.rId = rId;
    }

    public RecipeIngredient(Integer id, @NonNull String name, Integer quantity, String img, Integer rId) {
        super(id, name, quantity);
        this.img = img;
        this.rId = rId;
    }
}
