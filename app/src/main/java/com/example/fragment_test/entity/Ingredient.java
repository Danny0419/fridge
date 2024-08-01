package com.example.fragment_test.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import androidx.room.PrimaryKey;

public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    public Integer id;
    @ColumnInfo
    @NonNull
    public String name;
    @ColumnInfo
    @NonNull
    public String sort;
    @ColumnInfo
    public Integer quantity;

    public Ingredient() {
    }

    public Ingredient(Integer id, @NonNull String name, @NonNull String sort, Integer quantity) {
        this.id = id;
        this.name = name;
        this.sort = sort;
        this.quantity = quantity;
    }

    public Ingredient(String name, String sort, Integer quantity) {
        this.name = name;
        this.sort = sort;
        this.quantity = quantity;
    }

    @NonNull
    public String getSort() {
        return sort;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Ingredient setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
}
