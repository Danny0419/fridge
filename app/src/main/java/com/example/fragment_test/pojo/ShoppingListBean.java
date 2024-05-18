package com.example.fragment_test.pojo;

public class ShoppingListBean {
    public int id;
    public String name;
    public String category;
    public int quantity;

    public ShoppingListBean(int id, String name, String category, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }
}
