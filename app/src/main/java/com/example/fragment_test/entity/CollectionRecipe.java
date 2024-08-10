package com.example.fragment_test.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_collection", foreignKeys =
    @ForeignKey(entity = Recipe.class,
        parentColumns = "id",
        childColumns = "r_id",
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )
)
public class CollectionRecipe {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "r_id")
    public Integer rId;
    @Ignore
    public Recipe recipe;

    public CollectionRecipe(int id, Integer rId) {
        this.id = id;
        this.rId = rId;
    }

    @Ignore
    public CollectionRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
