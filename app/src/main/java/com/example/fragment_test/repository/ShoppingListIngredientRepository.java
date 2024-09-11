package com.example.fragment_test.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.ShoppingDAO;
import com.example.fragment_test.entity.Ingredient;
import com.example.fragment_test.entity.ShoppingIngredient;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShoppingListIngredientRepository {

    ShoppingDAO shoppingDAO;

    public static ShoppingListIngredientRepository shoppingListIngredientRepository;

    private ShoppingListIngredientRepository(Context context) {
        this.shoppingDAO = FridgeDatabase.getInstance(context).shoppingDAO();
    }

    public static synchronized ShoppingListIngredientRepository getInstance(Context context) {
        if (shoppingListIngredientRepository == null) {
            shoppingListIngredientRepository = new ShoppingListIngredientRepository(context);
        }

        return shoppingListIngredientRepository;
    }

    public List<ShoppingIngredient> addShoppingItem(ShoppingIngredient ingredient) {
        List<ShoppingIngredient> curShoppingList = shoppingDAO.getAllShoppingIngredients();

        if (!curShoppingList.isEmpty()) {
            List<String> collect = curShoppingList.stream()
                    .map(shoppingIngredient -> shoppingIngredient.name + shoppingIngredient.sort)
                    .toList();
            int i = collect.indexOf(ingredient.name + ingredient.sort);
            if (i > -1) {
                ShoppingIngredient shoppingIngredient = curShoppingList.get(i);
                ingredient.id = shoppingIngredient.id;
                ingredient.quantity = ingredient.quantity + shoppingIngredient.quantity;
            }
        }

        shoppingDAO.insertShoppingIngredient(ingredient);
        return shoppingDAO.getAllShoppingIngredients();
    }

    public List<ShoppingIngredient> getShoppingList() {
        return shoppingDAO.getAllShoppingIngredients();
    }

    public void check(List<Ingredient> ingredients) {
        List<ShoppingIngredient> shoppingList = shoppingListIngredientRepository.getShoppingList();
        List<Ingredient> collapseBuyAndShoppingList = new LinkedList<>(ingredients);
        collapseBuyAndShoppingList.addAll(shoppingList);

        Map<String, Ingredient> collect = computeSameIngredients(collapseBuyAndShoppingList);
        Map<String, Ingredient> finished = computeIsFinished(collect);
        Map<String, Ingredient> unfinished = computeIsUnfinished(collect);
        finished.forEach((key, value) -> shoppingDAO.updateItemStatusByName(key));
        unfinished.forEach((key, value) -> shoppingDAO.updateItemQuantityByName(value.name, value.quantity));
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

    public void removeShoppingItem(ShoppingIngredient shoppingIngredient) {
        shoppingDAO.deleteShoppingItem(shoppingIngredient);
    }
}
