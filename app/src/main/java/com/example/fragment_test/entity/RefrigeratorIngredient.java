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
    @ColumnInfo(name = "saving_day")
    public Integer savingDay;
    @ColumnInfo
    public String expiration;
    @ColumnInfo(defaultValue = "0")
    public Integer expired;

    @Ignore
    public RefrigeratorIngredient(int id, @NonNull String name, int quantity, Integer expired) {
        super(id, name, quantity);
        this.expired = expired;
    }

    @Ignore
    public RefrigeratorIngredient(@NonNull String name,  String sort, Integer quantity,  String img,  Integer savingDay,  String expiration,  Integer expired) {
        super(name, quantity);
        this.img = img;
        this.sort = sort;
        this.savingDay = savingDay;
        this.expiration = expiration;
        this.expired = expired;
    }

    public RefrigeratorIngredient(Integer id, @NonNull String name, Integer quantity,  String img, @NonNull String sort,  Integer savingDay, String expiration) {
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
