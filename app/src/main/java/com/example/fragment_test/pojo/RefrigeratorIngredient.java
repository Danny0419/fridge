package com.example.fragment_test.pojo;

import java.sql.Date;

public class RefrigeratorIngredient extends Ingredient{
    public String expiration;
    public Integer expired;

    public RefrigeratorIngredient(Integer id, String name, String img, String sort, Integer quantity, Integer savingDay, String expiration) {
        super(id, name, img, sort, quantity, savingDay);
        this.expiration = expiration;
    }
}
