package com.example.fragment_test.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class FridgeHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "fridge_db";
    private static final int DB_VERSION = 2;

    public FridgeHelper(Context context){
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createShoppingListTab(db);
        createScheduleTab(db);
        createRecipeCollectionTab(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ContentValues values = new ContentValues();
        values.put("name", "測試");
        values.put("category", "測試類");
        values.put("quantity", 1);
        db.insert("shopping_list", null, values);
    }

    private void createShoppingListTab(SQLiteDatabase db){
        String sql = "create table if not exists shopping_list (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(100) NOT null, " +
                "category VARCHAR(100) NOT null," +
                "quantity INTEGER NOT null)";

        db.execSQL(sql);
    }

    private void createScheduleTab(SQLiteDatabase db){
        String sql = "create table if not exists schedule (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "r_id INTEGER Not Null, " +
                "day INTEGER Not Null," +
                "cooking_date text not null)";

        db.execSQL(sql);
    }

    private void createRecipeCollectionTab(SQLiteDatabase db){
        String sql = "create table if not exists recipe_collection (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "r_id INTEGER Not Null )";

        db.execSQL(sql);
    }
}
