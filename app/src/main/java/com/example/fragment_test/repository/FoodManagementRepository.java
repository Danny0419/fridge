package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.constant.IngredientCategory;
import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RefrigeratorDAO;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.entity.RefrigeratorIngredientsMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FoodManagementRepository {
    RefrigeratorDAO refrigeratorDAO;
    RefrigeratorIngredientsMap map;

    public FoodManagementRepository(Context context) {
        this.refrigeratorDAO = FridgeDatabase.getInstance(context).refrigeratorDAO();
        map = new RefrigeratorIngredientsMap();
    }

    public Map<String, ArrayList<RefrigeratorIngredient>> getAllIngredients() {
        map.resetRefrigerator();
        List<RefrigeratorIngredient> list = refrigeratorDAO.getAllRefrigeratorIngredients();
        for (RefrigeratorIngredient value :
                list) {
            sortIngredients(value);
        }

        return map;
    }

    private void sortIngredients(RefrigeratorIngredient ingredient) {
        String category = ingredient.sort;
        if (category.contains("魚肉"))
            map.get(IngredientCategory.FISH.name).add(ingredient);
        else if (category.contains("肉"))
            map.get(IngredientCategory.MEAT.name).add(ingredient);
        else if (category.contains("蛋豆"))
            map.get(IngredientCategory.BEAN.name).add(ingredient);
        else if (category.contains("蔬菜"))
            map.get(IngredientCategory.VEGETABLE.name).add(ingredient);
    }
}