package com.example.fragment_test.repository;

import androidx.room.ExperimentalRoomApi;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.entity.Ingredient;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.entity.ShoppingIngredient;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ExperimentalRoomApi
@RunWith(AndroidJUnit4.class)
@SmallTest
public class RefrigeratorIngredientRepositoryTest extends TestCase {
    public FridgeDatabase database;
    public RefrigeratorIngredientRepository refrigeratorIngredientRepository;
    public ShoppingListIngredientRepository shoppingListIngredientRepository;

    @Before
    public void setUp() {
        refrigeratorIngredientRepository = RefrigeratorIngredientRepository.getInstance(ApplicationProvider.getApplicationContext());
        shoppingListIngredientRepository = ShoppingListIngredientRepository.getInstance(ApplicationProvider.getApplicationContext());
    }

    @After
    public void close() {
    }

    @Test
    public void addRefrigeratorIngredientsAndDeleteShoppingListItems(){
        List<ShoppingIngredient> shoppingIngredients = shoppingListIngredientRepository.getShoppingList();
        List<Ingredient> ingredients = new ArrayList<>(shoppingIngredients);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = timeFormatter.format(LocalDate.now());
        List<RefrigeratorIngredient> newIngredients = List.of(
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0)
        );

        ingredients.addAll(newIngredients);




    }
}
