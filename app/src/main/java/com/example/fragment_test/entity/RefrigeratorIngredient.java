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
    public Integer savingDay;

    public RefrigeratorIngredient(int id, @NonNull String name, int quantity, String img, String sort, Integer expiration) {
        super(id, name, quantity);
        this.img = img;
        this.sort = sort;
        this.expiration = expiration;
    }

    @Ignore
    public RefrigeratorIngredient(int id, @NonNull String name, int quantity, String img, String sort, Integer savingDay, Integer expiration) {
        super(id, name, quantity);
        this.img = img;
        this.sort = sort;
        this.savingDay = savingDay;
        this.expiration = expiration;
    }

    @NonNull
    public String getSort() {
        return sort;
    }
}
