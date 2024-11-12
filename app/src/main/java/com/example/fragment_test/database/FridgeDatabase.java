package com.example.fragment_test.database;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fragment_test.entity.Invoice;
import com.example.fragment_test.entity.InvoiceItem;
import com.example.fragment_test.entity.PreparedRecipe;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeIngredient;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.entity.ScheduleRecipe;
import com.example.fragment_test.entity.ShoppingIngredient;
import com.example.fragment_test.entity.Step;

import java.util.List;

@Database(entities = {
        Invoice.class,
        InvoiceItem.class,
        RefrigeratorIngredient.class,
        ShoppingIngredient.class,
        Recipe.class,
        Step.class,
        RecipeIngredient.class,
        ScheduleRecipe.class,
        PreparedRecipe.class,
        IngredientUsage.class},
        version = 1)
public abstract class FridgeDatabase extends RoomDatabase {

    private static volatile FridgeDatabase INSTANCE;

    public static final String DB_NAME = "fridge.db";

    public static FridgeDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FridgeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FridgeDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract InvoiceDAO invoiceDAO();
    public abstract InvoiceItemDAO invoiceItemDAO();
    public abstract RefrigeratorIngredientDAO refrigeratorIngredientDAO();
    public abstract ShoppingDAO shoppingDAO();
    public abstract RecipeDAO recipeDAO();
    public abstract StepDAO stepDAO();
    public abstract RecipeIngredientDAO recipeIngredientDAO();
    public abstract ScheduleRecipeDAO scheduleRecipeDAO();
    public abstract PreparedRecipeDAO preparedRecipeDAO();

    // 其他 DAO
    @Dao
    public interface InvoiceDAO {
        @Insert
        long insertInvoice(Invoice invoice);
        @Query("SELECT * FROM Invoice WHERE id = :invoiceId LIMIT 1")
        Invoice getInvoiceById(String invoiceId);
    }

    @Dao
    public interface InvoiceItemDAO {
        @Insert
        void insertInvoiceItems(List<InvoiceItem> invoiceItems);
    }
}



