package com.example.fragment_test.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.entity.ShoppingIngredient;

@Database(entities = {RefrigeratorIngredient.class, ShoppingIngredient.class}, version = 1)
public abstract class FridgeDatabase extends RoomDatabase {
    public static final String DB_NAME = "fridge.db";
    private static volatile FridgeDatabase instance;
    public static synchronized FridgeDatabase getInstance(Context context){
        if(instance == null){
            instance = create(context); //創立新的資料庫
        }
        return instance;
    }

    private static FridgeDatabase create(final Context context){
        return Room.databaseBuilder(context,FridgeDatabase.class,DB_NAME).build();
    }
    public abstract RefrigeratorDAO refrigeratorDAO();

    public abstract ShoppingDAO shoppingDAO();
}
