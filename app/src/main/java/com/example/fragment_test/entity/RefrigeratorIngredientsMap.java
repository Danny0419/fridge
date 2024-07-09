package com.example.fragment_test.entity;

import com.example.fragment_test.constant.IngredientCategory;

import java.util.ArrayList;
import java.util.HashMap;

public class RefrigeratorIngredientsMap extends HashMap<String, ArrayList<RefrigeratorIngredient>> {

    {
        for (IngredientCategory value :
                IngredientCategory.values()) {
            this.put(value.name, new ArrayList<>());
        }
    }
    public void resetRefrigerator() {
        for (IngredientCategory value :
                IngredientCategory.values()) {
            this.replace(value.name, new ArrayList<>());
        }
    }
}
