package com.example.fragment_test.database;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fragment_test.entity.ShoppingIngredient;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ShoppingDAOTest extends TestCase {

    public FridgeDatabase database;
    public ShoppingDAO shoppingDAO;

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        FridgeDatabase.class
                ).allowMainThreadQueries()
                .build();
        shoppingDAO = database.shoppingDAO();
    }

    @After
    public void close() {
        database.close();
    }

    @Test
    public void ADDANEWSHOPPINGITEM_RETURN1() {
        ShoppingIngredient shoppingIngredient = new ShoppingIngredient("牛排", "肉類", 1, 0);
        long i = shoppingDAO.insertShoppingIngredient(shoppingIngredient);
        assertEquals(1, i);

    }

    @Test
    public void updateShoppingListStatus(){
        List<ShoppingIngredient> shoppingIngredients = List.of(
                new ShoppingIngredient("牛排", "肉類", 1, 0),
                new ShoppingIngredient("牛肉卷", "肉類", 1, 0)
        );

        shoppingIngredients.forEach(shoppingIngredient -> shoppingDAO.insertShoppingIngredient(shoppingIngredient));

        shoppingDAO.updateItemStatusName("牛排");
        List<ShoppingIngredient> allShoppingIngredients = shoppingDAO.getAllShoppingIngredients();
        assertEquals(1, allShoppingIngredients.size());
        assertEquals("牛肉卷", allShoppingIngredients.get(0).name);
    }

    @Test
    public void updateItemQuantity(){
        List<ShoppingIngredient> shoppingIngredients = List.of(
                new ShoppingIngredient("牛排", "肉類", 1, 0),
                new ShoppingIngredient("牛肉卷", "肉類", 1, 0)
        );

        shoppingIngredients.forEach(shoppingIngredient -> shoppingDAO.insertShoppingIngredient(shoppingIngredient));
        List<ShoppingIngredient> allShoppingIngredients1 = shoppingDAO.getAllShoppingIngredients();
        shoppingDAO.updateItemQuantityByName("牛排",3);
        List<ShoppingIngredient> allShoppingIngredients = shoppingDAO.getAllShoppingIngredients();
        assertEquals("牛排", allShoppingIngredients.get(0).name);
        assertEquals(3, allShoppingIngredients.get(0).quantity);
    }
}