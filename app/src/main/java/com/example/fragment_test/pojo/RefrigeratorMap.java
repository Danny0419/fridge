package com.example.fragment_test.pojo;

import com.example.fragment_test.constant.IngredientCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RefrigeratorMap {
    public static Map<String, ArrayList<RefrigeratorIngredient>> map = new HashMap<>();

    static {
        map.put(IngredientCategory.MEAT.name, IngredientCategory.MEAT.refrigeratorIngredients);
        map.put(IngredientCategory.FISH.name, IngredientCategory.FISH.refrigeratorIngredients);
        map.put(IngredientCategory.BEAN.name, IngredientCategory.BEAN.refrigeratorIngredients);
        map.put(IngredientCategory.VEGETABLE.name, IngredientCategory.VEGETABLE.refrigeratorIngredients);
    }
}
