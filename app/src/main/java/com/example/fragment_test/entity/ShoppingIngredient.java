package com.example.fragment_test.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "shopping_list")
public class ShoppingIngredient extends Ingredient {

    @ColumnInfo
    @NonNull
    public String sort;
    @ColumnInfo(defaultValue = "0")
    public Integer status;

    @Ignore
    public ShoppingIngredient(@NonNull String name, Integer quantity, @NonNull String sort) {
        super(name, quantity);
        this.sort = sort;
    }

    public ShoppingIngredient(@NonNull String name, @NonNull String sort, Integer quantity, Integer status) {
        super(name, quantity);
        this.sort = sort;
        this.status = status;
    }
}
