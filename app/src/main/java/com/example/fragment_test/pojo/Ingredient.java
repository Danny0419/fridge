package com.example.fragment_test.pojo;

public class Ingredient {
    public Integer id;
    public String name;
    public String img;
    public String sort;
    public Integer quantity;
    public Integer savingDay;

    public Ingredient() {
    }

    public Ingredient(String name, String img) {
        this.name = name;
        this.img = img;
    }



    public Ingredient(Integer id, String name, String img, String sort) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.sort = sort;
    }

    public Ingredient(Integer id, String name, String img, String sort, Integer quantity) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.sort = sort;
        this.quantity = quantity;
    }

    public Ingredient(Integer id, String name, String img, String sort, Integer quantity, Integer savingDay) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.sort = sort;
        this.quantity = quantity;
        this.savingDay = savingDay;
    }
}
