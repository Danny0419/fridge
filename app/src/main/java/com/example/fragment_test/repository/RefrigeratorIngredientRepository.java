package com.example.fragment_test.repository;


import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RefrigeratorIngredientDAO;
import com.example.fragment_test.entity.Ingredient;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.entity.ShoppingIngredient;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class RefrigeratorIngredientRepository {
    RefrigeratorIngredientDAO refrigeratorIngredientDAO;
    ShoppingListIngredientRepository shoppingListIngredientRepository;
    public static RefrigeratorIngredientRepository refrigeratorIngredientRepository;

    private RefrigeratorIngredientRepository(Context context) {
        this.refrigeratorIngredientDAO = FridgeDatabase.getInstance(context).refrigeratorDAO();
        this.shoppingListIngredientRepository = ShoppingListIngredientRepository.getInstance(context);
    }

    public static RefrigeratorIngredientRepository getInstance(Context context) {
        if (refrigeratorIngredientRepository == null) {
            refrigeratorIngredientRepository = new RefrigeratorIngredientRepository(context);
        }
        return refrigeratorIngredientRepository;
    }

    public long[] addRefrigeratorIngredients(List<RefrigeratorIngredient> ingredients) {
        long[] result = refrigeratorIngredientDAO.insertIngredients(ingredients);

        ingredients.forEach(ingredient -> ingredient.setQuantity(-ingredient.quantity));
        List<Ingredient> match = new LinkedList<>(ingredients);

        List<ShoppingIngredient> shoppingList = shoppingListIngredientRepository.getShoppingList();
        match.addAll(shoppingList);

        Map<String, Ingredient> collect = match.stream()
                .collect(Collectors.toMap(Ingredient::getName, a -> a,
                        (o1, o2) -> o1.setQuantity(o1.quantity + o2.quantity)));

        Map<String, Ingredient> finished = collect.entrySet()
                .stream()
                .filter(entry -> entry.getValue().quantity <= 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Ingredient> unfinished = collect.entrySet()
                .stream()
                .filter(entry -> entry.getValue().quantity > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        shoppingListIngredientRepository.check(finished, unfinished);
        return result;
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
