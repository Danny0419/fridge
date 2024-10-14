package com.example.fragment_test.database;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fragment_test.entity.Invoice;
import com.example.fragment_test.entity.InvoiceItem;
import com.example.fragment_test.entity.PreparedRecipe;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeIngredient;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.entity.Schedule;
import com.example.fragment_test.entity.ScheduleRecipe;
import com.example.fragment_test.entity.ShoppingIngredient;
import com.example.fragment_test.entity.Step;

import java.util.List;

@Database(entities = {
        Invoice.class,
        InvoiceItem.class,
        RefrigeratorIngredient.class,
        ShoppingIngredient.class,
        Schedule.class,
        Recipe.class,
        Step.class,
        RecipeIngredient.class,
        ScheduleRecipe.class,
        PreparedRecipe.class},
        version = 1)
public abstract class FridgeDatabase extends RoomDatabase {
    public abstract InvoiceDAO invoiceDAO();
    public abstract InvoiceItemDAO invoiceItemDAO();

    private static volatile FridgeDatabase INSTANCE;

    public static FridgeDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FridgeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FridgeDatabase.class, "fridge_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    public static final String DB_NAME = "fridge.db";


    // 创建数据库
    private static FridgeDatabase create(final Context context){
        return Room.databaseBuilder(context, FridgeDatabase.class, DB_NAME).build();
    }

    // 关闭数据库
    public void close(){
        super.close();
    }

    // 添加 Invoice 和 InvoiceItem 的 DAO
    @Dao
    public interface InvoiceDAO {
        @Insert
        long insertInvoice(Invoice invoice);
    }

    @Dao
    public interface InvoiceItemDAO {
        @Insert
        void insertInvoiceItems(List<InvoiceItem> invoiceItems);
    }

    // 其他 DAO
    public abstract RefrigeratorIngredientDAO refrigeratorDAO();
    public abstract ShoppingDAO shoppingDAO();
    public abstract ScheduleDAO scheduleDAO();
    public abstract RecipeDAO recipeDAO();
    public abstract StepDAO stepDAO();
    public abstract RecipeIngredientDAO recipeIngredientDAO();
    public abstract ScheduleRecipeDAO scheduleRecipeDAO();
    public abstract PreparedRecipeDAO preparedRecipeDAO();

}
