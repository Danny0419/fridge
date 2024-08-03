package com.example.fragment_test.entity;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class RecipeIngredient {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String img;
    @ColumnInfo
    public int quantity;
}
