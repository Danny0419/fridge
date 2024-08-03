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
    public Integer quantity;

    public Ingredient() {
    }

    public Ingredient(Integer id, @NonNull String name, Integer quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public Ingredient(@NonNull String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
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
