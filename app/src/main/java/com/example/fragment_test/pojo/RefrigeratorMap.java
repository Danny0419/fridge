package com.example.fragment_test.pojo;

import com.example.fragment_test.constant.IngredientCategory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RefrigeratorMap {
    public static Map<String, ArrayList<RefrigeratorIngredient>> map = new HashMap<>();

    static {
        resetRefrigerator();
    }

    public static void resetRefrigerator() {
        map = new HashMap<>();
        for (IngredientCategory value :
                IngredientCategory.values()) {
            map.put(value.name, new ArrayList<>());
        }
    }
}
