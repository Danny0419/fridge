package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.ShoppingDAO;
import com.example.fragment_test.entity.ShoppingIngredient;

import java.util.List;
import java.util.Optional;

public class ShoppingRepository {
    private ShoppingDAO shoppingDAO;

    public ShoppingRepository(Context context){
        shoppingDAO = FridgeDatabase.getInstance(context).shoppingDAO();
    }

    public List<ShoppingIngredient> getAllIngredients() {
        return shoppingDAO.getAllShoppingIngredients();
    }
}
