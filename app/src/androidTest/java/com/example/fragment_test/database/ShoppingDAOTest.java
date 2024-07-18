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
}