package com.example.fragment_test.constant;

public enum IngredientCategory {
    MEAT("肉類"), FISH("魚肉類"), BEAN("豆蛋類"), VEGETABLE("蔬菜類");

    public final String name;

    IngredientCategory(String name){
        this.name = name;
    }
}
