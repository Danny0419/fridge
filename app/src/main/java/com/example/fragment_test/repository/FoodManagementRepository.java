package com.example.fragment_test.repository;



import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RefrigeratorDAO;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.entity.RefrigeratorIngredientsMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class FoodManagementRepository {
    RefrigeratorDAO refrigeratorDAO;

    public FoodManagementRepository(Context context) {
        this.refrigeratorDAO = FridgeDatabase.getInstance(context).refrigeratorDAO();
    }

    public Map<String, List<RefrigeratorIngredient>> getAllIngredients() {
        List<RefrigeratorIngredient> list = refrigeratorDAO.getAllRefrigeratorIngredients();
        return sortIngredients(list);
    }

    private Map<String, List<RefrigeratorIngredient>> sortIngredients(List<RefrigeratorIngredient> list) {
        Map<String, List<RefrigeratorIngredient>> refrigeratorMap = list.stream()
                .collect(Collectors.groupingBy(RefrigeratorIngredient::getSort));

        return refrigeratorMap;
    }
}
