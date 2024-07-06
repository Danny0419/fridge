package com.example.fragment_test.pojo;

import androidx.room.ColumnInfo;

import androidx.room.PrimaryKey;

public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    public Integer id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String sort;
    @ColumnInfo
    public Integer quantity;

    public Ingredient() {
    }

    public Ingredient(String name) {
        this.name = name;
    }

    public Ingredient(Integer id, String name, String sort, Integer quantity) {
        this.id = id;
        this.name = name;
        this.sort = sort;
        this.quantity = quantity;
    }

    public Ingredient(Integer id, String name, String img, String sort) {
        this.id = id;
        this.name = name;
        this.sort = sort;
    }

    public Ingredient(Integer id, String name, Integer quantity, String sort) {
        this.id = id;
        this.name = name;
        this.sort = sort;
        this.quantity = quantity;
    }

    public Ingredient(Integer id, String name, String sort) {
        this.id = id;
        this.name = name;
        this.sort = sort;
    }

    public Ingredient(String name, String sort, Integer quantity) {
        this.name = name;
        this.sort = sort;
        this.quantity = quantity;
    }
}
