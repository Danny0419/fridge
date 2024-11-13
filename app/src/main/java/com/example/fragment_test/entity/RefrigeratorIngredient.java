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
    @Ignore
    public int daysRemaining;

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

    public void setDaysRemaining(int daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    public String getPurchaseDate(){
        String stringPurchaseDate= String.valueOf(purchaseDate);

        String year = stringPurchaseDate.substring(2, 4);
        String month = stringPurchaseDate.substring(4, 6);
        String day = stringPurchaseDate.substring(6, 8);

        stringPurchaseDate = year + "/" + month + "/" + day;
        return stringPurchaseDate;
    }

    public String getExpiration(){
        String stringExpiration= String.valueOf(expiration);

        String year = stringExpiration.substring(2, 4);
        String month = stringExpiration.substring(4, 6);
        String day = stringExpiration.substring(6, 8);

        stringExpiration = year + "/" + month + "/" + day;
        return stringExpiration;
    }
}
