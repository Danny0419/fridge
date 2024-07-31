package com.example.fragment_test.repository;


import android.content.Context;

import androidx.annotation.NonNull;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RefrigeratorIngredientDAO;
import com.example.fragment_test.entity.Ingredient;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.entity.ShoppingIngredient;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RefrigeratorIngredientRepository {
    private final RefrigeratorIngredientDAO refrigeratorIngredientDAO;
    private final ShoppingListIngredientRepository shoppingListIngredientRepository;
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

    public long[] buyIngredients(List<RefrigeratorIngredient> ingredients) {
        long[] result = refrigeratorIngredientDAO.insertIngredients(ingredients);

        ingredients.forEach(ingredient -> ingredient.setQuantity(-ingredient.quantity));

        List<ShoppingIngredient> shoppingList = shoppingListIngredientRepository.getShoppingList();

        List<Ingredient> collapse = new LinkedList<>(ingredients);
        collapse.addAll(shoppingList);

        Map<String, Ingredient> collect = computeSameIngredients(collapse);

        Map<String, Ingredient> finished = sortIngredientsIntoFinished(collect);

        Map<String, Ingredient> unfinished = sortIngredientsIntoUnfinished(collect);

        shoppingListIngredientRepository.check(finished, unfinished);
        return result;
    }

    private @NonNull Map<String, Ingredient> sortIngredientsIntoUnfinished(Map<String, Ingredient> collect) {
        return collect.entrySet()
                .stream()
                .filter(entry -> entry.getValue().quantity > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private @NonNull Map<String, Ingredient> sortIngredientsIntoFinished(Map<String, Ingredient> collect) {
        return collect.entrySet()
                .stream()
                .filter(entry -> entry.getValue().quantity <= 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static @NonNull Map<String, Ingredient> computeSameIngredients(List<Ingredient> collapse) {
        return collapse.stream()
                .collect(Collectors.toMap(Ingredient::getName, a -> a,
                        (o1, o2) -> o1.setQuantity(o1.quantity + o2.quantity)));
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
