package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fragment_test.entity.ShoppingIngredient;
import com.example.fragment_test.vo.ShoppingItemVO;

import java.util.List;

@Dao
public interface ShoppingDAO {
    @Query("""
            select id, name, sort, quantity, status from shopping_list
            where status = 0 and quantity > 0;
            """)
    List<ShoppingIngredient> getAllShoppingIngredients();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertShoppingIngredient(ShoppingIngredient shoppingIngredient);

    @Query("""
            UPDATE shopping_list SET status = 1
            WHERE name = :name
            """)
    void updateItemStatusByName(String name);

    @Query("""
            UPDATE shopping_list SET quantity = :quantity
            WHERE name = :name
            """)
    void updateItemQuantityByName(String name, int quantity);

    @Delete
    int deleteShoppingItem(ShoppingIngredient shoppingIngredient);

    @Query(
            """
            SELECT name, sort, sum(quantity) as sumOfQuantity
            FROM shopping_list
            GROUP BY name, sort
            HAVING sumOfQuantity > 0
            """
    )
    List<ShoppingItemVO> getAllSumOfQuaGreaterThanZeroShoppingIngredientsGroupByName();
}
