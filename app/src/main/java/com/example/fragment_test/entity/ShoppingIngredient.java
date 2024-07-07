package com.example.fragment_test.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "shopping_list")
public class ShoppingIngredient extends Ingredient{

    @ColumnInfo
    public Integer status;


    public ShoppingIngredient(Integer id, String name, String sort, Integer quantity) {
        super(id, name, sort, quantity);
    }
}
