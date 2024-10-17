package com.example.fragment_test.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "refrigerator")
public class RefrigeratorIngredient extends Ingredient {
    @ColumnInfo
    public String img;
    @ColumnInfo
    public String sort;
    @ColumnInfo(name = "purchase_date")
    public Integer purchaseDate;
    @ColumnInfo
    public Integer expiration;
    @ColumnInfo
    public String unit;
    @Ignore
    public Integer savingDay;

    public RefrigeratorIngredient(int id, @NonNull String name, int quantity, String img, String sort, Integer purchaseDate, Integer expiration, String unit) {
        super(id, name, quantity);
        this.img = img;
        this.sort = sort;
        this.purchaseDate = purchaseDate;
        this.expiration = expiration;
        this.unit = unit;
    }

    @Ignore
    public RefrigeratorIngredient(int id, @NonNull String name, int quantity, String img, String sort, Integer purchaseDate, Integer expiration) {
        super(id, name, quantity);
        this.img = img;
        this.sort = sort;
        this.purchaseDate = purchaseDate;
        this.expiration = expiration;
    }

    @NonNull
    public String getSort() {
        return sort;
    }
}
