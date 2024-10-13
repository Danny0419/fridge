package com.example.fragment_test.vo;

public class ShoppingItemVO {
    public String name;
    public String sort;
    public int sumOfQuantity;

    public ShoppingItemVO(String name, String sort, int sumOfQuantity) {
        this.name = name;
        this.sort = sort;
        this.sumOfQuantity = sumOfQuantity;
    }

    public String getName() {
        return name;
    }
}
