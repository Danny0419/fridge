package com.example.fragment_test.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "shopping_list")
public class ShoppingIngredient extends Ingredient {

    @ColumnInfo(defaultValue = "0")
    public Integer status;

    public ShoppingIngredient(String name, String sort, Integer quantity) {
        super(name, sort, quantity);
    }

    @Ignore
    public ShoppingIngredient(String name, String sort, Integer quantity, Integer status) {
        super(name, sort, quantity);
        this.status = status;
    }
}
