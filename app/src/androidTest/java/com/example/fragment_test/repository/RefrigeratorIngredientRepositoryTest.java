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
import java.util.Map;

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
    public void buyThreeSteakShoppingListSizeShouldEqualFour() {

        List<ShoppingIngredient> shoppingIngredients = List.of(
                new ShoppingIngredient("牛排", "肉類", 3, 0),
                new ShoppingIngredient("牛肉卷", "肉類", 2, 0),
                new ShoppingIngredient("高麗菜", "蔬菜類", 5, 0),
                new ShoppingIngredient("豬排", "肉類", 1, 0),
                new ShoppingIngredient("五花豬", "肉類", 3, 0)
        );
        shoppingIngredients.forEach(shoppingIngredient -> shoppingListIngredientRepository.addShoppingItem(shoppingIngredient));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = timeFormatter.format(LocalDate.now());
        List<RefrigeratorIngredient> newIngredients = List.of(
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0)
        );

        refrigeratorIngredientRepository.addRefrigeratorIngredients(newIngredients);
        List<ShoppingIngredient> shoppingList = shoppingListIngredientRepository.getShoppingList();
        assertEquals(4, shoppingList.size());


    }

    @Test
    public void buyThreeSteakAndOneBeefRollShoppingListSizeShouldEqualFourAndBeefRollItemQuantityShouldEqualOne() {

        List<ShoppingIngredient> shoppingIngredients = List.of(
                new ShoppingIngredient("牛排", "肉類", 3, 0),
                new ShoppingIngredient("牛肉卷", "肉類", 2, 0),
                new ShoppingIngredient("高麗菜", "蔬菜類", 5, 0),
                new ShoppingIngredient("豬排", "肉類", 1, 0),
                new ShoppingIngredient("五花豬", "肉類", 3, 0)
        );
        shoppingIngredients.forEach(shoppingIngredient -> shoppingListIngredientRepository.addShoppingItem(shoppingIngredient));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = timeFormatter.format(LocalDate.now());
        List<RefrigeratorIngredient> newIngredients = List.of(
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛肉卷", "肉類", 1, "牛排照片", 5, time, 0)
        );

        refrigeratorIngredientRepository.addRefrigeratorIngredients(newIngredients);

        Map<String, List<RefrigeratorIngredient>> allIngredients = refrigeratorIngredientRepository.getAllIngredients();
        assertEquals(1, allIngredients.size());
        assertEquals(2, allIngredients.get("肉類").size());

        List<ShoppingIngredient> shoppingList = shoppingListIngredientRepository.getShoppingList();
        assertEquals(4, shoppingList.size());
        assertEquals("牛肉卷", shoppingList.get(0).name);
        assertEquals(1, (shoppingList.get(0).quantity.intValue()));
    }

    @Test
    public void buyThreeSteakAndTwoBeefRollAndOneCabbageShoppingListSizeShouldEqualThree() {

        List<ShoppingIngredient> shoppingIngredients = List.of(
                new ShoppingIngredient("牛排", "肉類", 3, 0),
                new ShoppingIngredient("牛肉卷", "肉類", 2, 0),
                new ShoppingIngredient("高麗菜", "蔬菜類", 5, 0),
                new ShoppingIngredient("豬排", "肉類", 1, 0),
                new ShoppingIngredient("五花豬", "肉類", 3, 0)
        );
        shoppingIngredients.forEach(shoppingIngredient -> shoppingListIngredientRepository.addShoppingItem(shoppingIngredient));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = timeFormatter.format(LocalDate.now());
        List<RefrigeratorIngredient> newIngredients = List.of(
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛肉卷", "肉類", 2, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("高麗菜", "蔬菜類", 1, "高麗菜照片", 5, time, 0)
        );

        refrigeratorIngredientRepository.addRefrigeratorIngredients(newIngredients);

        Map<String, List<RefrigeratorIngredient>> allIngredients = refrigeratorIngredientRepository.getAllIngredients();
        assertEquals(2, allIngredients.size());
        assertEquals(2, allIngredients.get("肉類").size());
        assertEquals(1, allIngredients.get("蔬菜類").size());

        List<ShoppingIngredient> shoppingList = shoppingListIngredientRepository.getShoppingList();
        assertEquals(3, shoppingList.size());
        assertEquals("高麗菜", shoppingList.get(0).name);
        assertEquals(4, (shoppingList.get(0).quantity.intValue()));
    }
}
