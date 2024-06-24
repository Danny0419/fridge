package com.example.fragment_test.constant;

import com.example.fragment_test.pojo.RefrigeratorIngredient;

import java.util.ArrayList;

public enum IngredientCategory {
    MEAT("肉類"), FISH("魚類"), BEAN("豆類"), VEGETABLE("蔬菜類");

    public String name;
    public final ArrayList<RefrigeratorIngredient> refrigeratorIngredients;

    IngredientCategory(String name){
        this.name = name;
        refrigeratorIngredients = new ArrayList<>();
    }
}
