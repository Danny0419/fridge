package com.example.fragment_test.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "refrigerator")
public class RefrigeratorIngredient extends Ingredient {
    @ColumnInfo
    @NonNull
    public String img;
    @ColumnInfo(name = "saving_day")
    @NonNull
    public Integer savingDay;
    @ColumnInfo
    @NonNull
    public String expiration;
    @ColumnInfo(defaultValue = "0")
    public Integer expired;

    @Ignore
    public RefrigeratorIngredient(String name, String sort, Integer quantity, @NonNull String img, @NonNull Integer savingDay, @NonNull String expiration) {
        super(name, sort, quantity);
        this.img = img;
        this.savingDay = savingDay;
        this.expiration = expiration;
    }

    public RefrigeratorIngredient(Integer id, String name, String img, Integer quantity, String sort, Integer savingDay, String expiration) {
        super(id, name, sort, quantity);
        this.img = img;
        this.savingDay = savingDay;
        this.expiration = expiration;
    }


}
