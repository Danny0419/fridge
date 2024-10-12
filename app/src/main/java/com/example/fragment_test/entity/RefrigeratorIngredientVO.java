package com.example.fragment_test.entity;

public class RefrigeratorIngredientVO {
    public String name;
    public String sort;
    public String img;
    public int earlyEx;
    public int lastEx;
    public int sumQuantity;

    public RefrigeratorIngredientVO(String name, String sort, String img, int earlyEx, int lastEx, int sumQuantity) {
        this.name = name;
        this.sort = sort;
        this.img = img;
        this.earlyEx = earlyEx;
        this.lastEx = lastEx;
        this.sumQuantity = sumQuantity;
    }

    public String getName() {
        return name;
    }

    public String getSort() {
        return sort;
    }
}
