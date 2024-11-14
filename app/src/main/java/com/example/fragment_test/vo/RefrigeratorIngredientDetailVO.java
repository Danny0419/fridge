package com.example.fragment_test.vo;

import androidx.room.Ignore;

public class RefrigeratorIngredientDetailVO {
    public String name;
    public int quantity;
    public int purchaseDate;
    public int expiration;
    public int daysRemaining;


    public RefrigeratorIngredientDetailVO(String name, int quantity, int purchaseDate, int expiration) {
        this.name = name;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
        this.expiration = expiration;
    }

    @Ignore
    public RefrigeratorIngredientDetailVO(int quantity, int purchaseDate, int expiration, int daysRemaining) {
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
        this.expiration = expiration;
        this.daysRemaining = daysRemaining;
    }
}
