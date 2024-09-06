package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.ShoppingDAO;
import com.example.fragment_test.entity.Ingredient;
import com.example.fragment_test.entity.ShoppingIngredient;

import java.util.List;
import java.util.Map;

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

    public void check(Map<String, Ingredient> finished, Map<String, Ingredient> unfinished) {
        finished.forEach((key, value) -> shoppingDAO.updateItemStatusByName(key));
        unfinished.forEach((key, value) -> shoppingDAO.updateItemQuantityByName(value.name, value.quantity));
    }

    public void removeShoppingItem(ShoppingIngredient shoppingIngredient) {
        shoppingDAO.deleteShoppingItem(shoppingIngredient);
    }
}
