package com.example.fragment_test.entity;

import androidx.room.Ignore;

public class RefrigeratorIngredientDetailVO {
    public int quantity;
    public int purchaseDate;
    public int expiration;
    public int daysRemaining;


    public RefrigeratorIngredientDetailVO(int quantity, int purchaseDate, int expiration) {
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
