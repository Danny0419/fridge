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
    private static RefrigeratorIngredientRepository refrigeratorIngredientRepository;

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

    public void buyIngredients(List<RefrigeratorIngredient> ingredients) {

        List<RefrigeratorIngredient> curRefrigeratorIng = refrigeratorIngredientDAO.getAllRefrigeratorIngredients();
        List<RefrigeratorIngredient> collapseFriAndBuy = new LinkedList<>(curRefrigeratorIng);
        collapseFriAndBuy.addAll(ingredients);

        Map<String, RefrigeratorIngredient> sameSortAndEx = collapseFriAndBuy.stream()
                .collect(Collectors.toMap(this::fetchGroupKey, o -> o, (o1, o2) -> (RefrigeratorIngredient) o1.setQuantity(o1.quantity + o2.quantity)));

        sameSortAndEx.forEach((key, value) -> refrigeratorIngredientDAO.insertIngredient(value));


        ingredients.forEach(ingredient -> ingredient.setQuantity(-ingredient.quantity));

        List<ShoppingIngredient> shoppingList = shoppingListIngredientRepository.getShoppingList();
        List<Ingredient> collapseBuyAndShoppingList = new LinkedList<>(ingredients);
        collapseBuyAndShoppingList.addAll(shoppingList);

        Map<String, Ingredient> collect = computeSameIngredients(collapseBuyAndShoppingList);
        Map<String, Ingredient> finished = computeIsFinished(collect);
        Map<String, Ingredient> unfinished = computeIsUnfinished(collect);

        shoppingListIngredientRepository.check(finished, unfinished);
    }

    private String fetchGroupKey(RefrigeratorIngredient ingredient){
        return ingredient.name +"#"+ ingredient.expiration;
    }

    private @NonNull Map<String, Ingredient> computeIsUnfinished(Map<String, Ingredient> collect) {
        return collect.entrySet()
                .stream()
                .filter(entry -> entry.getValue().quantity > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private @NonNull Map<String, Ingredient> computeIsFinished(Map<String, Ingredient> collect) {
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
