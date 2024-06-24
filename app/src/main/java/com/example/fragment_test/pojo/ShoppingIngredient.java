package com.example.fragment_test.pojo;

public class ShoppingIngredient extends Ingredient{

    public Integer status;

    public ShoppingIngredient(Integer id, String name, String img, String sort, Integer status) {
        super(id, name, img, sort);
        this.status = status;
    }
}
