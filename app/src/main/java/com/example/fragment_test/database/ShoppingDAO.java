package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fragment_test.entity.ShoppingIngredient;

import java.util.List;

@Dao
public interface ShoppingDAO {
    @Query("""
            select name, sort, quantity from shopping_list
            where status = 0;
            """)
    List<ShoppingIngredient> getAllShoppingIngredients();

    @Insert
    long insertShoppingIngredient(ShoppingIngredient shoppingIngredient);

    @Query("""
            UPDATE shopping_list SET status = 1
            WHERE name = :name
            """)
    void updateItemStatusName(String name);

    @Query("""
            UPDATE shopping_list SET quantity = :quantity
            WHERE name = :name
            """)
    void updateItemQuantityByName(String name, int quantity);
}
