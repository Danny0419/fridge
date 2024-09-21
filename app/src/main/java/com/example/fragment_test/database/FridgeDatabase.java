package com.example.fragment_test.database;

import android.content.Context;

import androidx.room.Database;
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
        PreparedRecipe.class,
        IngredientUsage.class},
        version = 1)
public abstract class FridgeDatabase extends RoomDatabase {
    public static final String DB_NAME = "fridge.db";
    private static volatile FridgeDatabase instance;

    // 获取单例实例
    public static synchronized FridgeDatabase getInstance(Context context){
        if(instance == null){
            instance = create(context); // 创建新的数据库
        }
        return instance;
    }

    // 创建数据库
    private static FridgeDatabase create(final Context context){
        return Room.databaseBuilder(context, FridgeDatabase.class, DB_NAME).build();
    }

    // 关闭数据库
    public void close(){
        super.close();
    }

    // 添加 Invoice 和 InvoiceItem 的 DAO
    public abstract InvoiceDAO invoiceDAO();
    public abstract InvoiceItemDAO invoiceItemDAO();

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
