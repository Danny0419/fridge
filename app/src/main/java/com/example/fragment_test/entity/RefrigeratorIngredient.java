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
    @ColumnInfo
    @NonNull
    public String sort;
    @ColumnInfo(name = "saving_day")
    @NonNull
    public Integer savingDay;
    @ColumnInfo
    @NonNull
    public String expiration;
    @ColumnInfo(defaultValue = "0")
    @NonNull
    public Integer expired;

    @Ignore
    public RefrigeratorIngredient(@NonNull String name, Integer quantity, @NonNull String img, @NonNull String sort, @NonNull Integer savingDay, @NonNull String expiration, @NonNull Integer expired) {
        super(name, quantity);
        this.img = img;
        this.sort = sort;
        this.savingDay = savingDay;
        this.expiration = expiration;
        this.expired = expired;
    }

    public RefrigeratorIngredient(Integer id, @NonNull String name, Integer quantity, @NonNull String img, @NonNull String sort, @NonNull Integer savingDay, @NonNull String expiration) {
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
