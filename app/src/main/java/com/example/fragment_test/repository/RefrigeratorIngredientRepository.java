package com.example.fragment_test.repository;



import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RefrigeratorIngredientDAO;
import com.example.fragment_test.entity.RefrigeratorIngredient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RefrigeratorIngredientRepository {
    RefrigeratorIngredientDAO refrigeratorIngredientDAO;

    public static RefrigeratorIngredientRepository  refrigeratorIngredientRepository;
    private RefrigeratorIngredientRepository(Context context) {
        this.refrigeratorIngredientDAO = FridgeDatabase.getInstance(context).refrigeratorDAO();
    }

    public static RefrigeratorIngredientRepository getInstance(Context context){
        if (refrigeratorIngredientRepository == null){
            refrigeratorIngredientRepository = new RefrigeratorIngredientRepository(context);
        }

        return refrigeratorIngredientRepository;
    }

    public Map<String, List<RefrigeratorIngredient>> getAllIngredients() {
        List<RefrigeratorIngredient> list = refrigeratorIngredientDAO.getAllRefrigeratorIngredients();
        return sortIngredients(list);
    }

    private Map<String, List<RefrigeratorIngredient>> sortIngredients(List<RefrigeratorIngredient> list) {
        Map<String, List<RefrigeratorIngredient>> refrigeratorMap = list.stream()
                .collect(Collectors.groupingBy(RefrigeratorIngredient::getSort));

        return refrigeratorMap;
    }
}
