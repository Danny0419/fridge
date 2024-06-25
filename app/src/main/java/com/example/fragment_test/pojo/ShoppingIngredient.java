package com.example.fragment_test.pojo;

public class ShoppingIngredient extends Ingredient{

    public Integer status;


    public ShoppingIngredient(Integer id, String name, String sort, Integer quantity) {
        super(id, name, sort, quantity);
    }

    public ShoppingIngredient(Integer id, String name, String img, String sort, Integer quantity , Integer status) {
        super(id, name, img, quantity, sort);
        this.status = status;
    }
}
