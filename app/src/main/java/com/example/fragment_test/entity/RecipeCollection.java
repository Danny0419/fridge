package com.example.fragment_test.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_collection")
public class RecipeCollection {
    @PrimaryKey
    public int id;
    @ColumnInfo
    public String name;

}
