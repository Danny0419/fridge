package com.example.fragment_test.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredients_usage")
public class IngredientUsage {

    @PrimaryKey
    @NonNull
    public String name;

    @ColumnInfo
    public int available;

    @ColumnInfo
    public int using;

    public IngredientUsage(@NonNull String name, int available, int using) {
        this.name = name;
        this.available = available;
        this.using = using;
    }

}
