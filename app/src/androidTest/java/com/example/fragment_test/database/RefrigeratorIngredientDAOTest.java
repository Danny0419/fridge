package com.example.fragment_test.database;

import androidx.room.ExperimentalRoomApi;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.example.fragment_test.entity.RefrigeratorIngredient;

import junit.framework.TestCase;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ExperimentalRoomApi
@RunWith(AndroidJUnit4.class)
@SmallTest
public class RefrigeratorIngredientDAOTest extends TestCase {
    public FridgeDatabase database;
    public RefrigeratorIngredientDAO refrigeratorIngredientDAO;

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        FridgeDatabase.class
                ).allowMainThreadQueries()
                .build();
        refrigeratorIngredientDAO = database.refrigeratorDAO();
    }

    @After
    public void close() {
        database.close();
    }

    @Test
    public void insertNewAIngredientToRefrigerator() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = timeFormatter.format(LocalDate.now());
        List<RefrigeratorIngredient> ingredients = List.of(
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0)
        );
        long[] i = refrigeratorIngredientDAO.insertIngredients(ingredients);

        assertEquals(i[0], 1);
        assertEquals(i[1], 2);
    }

    @Test
    public void insertnewfiveingredientstorefrigerator_returnOneToFive() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = timeFormatter.format(LocalDate.now());
        List<RefrigeratorIngredient> ingredients = List.of(
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0)
        );

        long[] i = refrigeratorIngredientDAO.insertIngredients(ingredients);

        assertEquals(1, i[0]);
        assertEquals(2, i[1]);
        assertEquals(3, i[2]);
        assertEquals(4, i[3]);
        assertEquals(5, i[4]);
    }

    @Test
    public void QueryNotExpiredIngredients_ReturnSixItemOfIngredientsList() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = timeFormatter.format(LocalDate.now());
        List<RefrigeratorIngredient> ingredients = List.of(
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0),
                new RefrigeratorIngredient("牛排", "肉類", 3, "牛排照片", 5, time, 0)
        );
        refrigeratorIngredientDAO.insertIngredients(ingredients);
        
        List<RefrigeratorIngredient> list = refrigeratorIngredientDAO.getAllRefrigeratorIngredients();

        assertEquals(6 ,list.size());
    }

}