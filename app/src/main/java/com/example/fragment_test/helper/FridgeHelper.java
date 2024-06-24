package com.example.fragment_test.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class FridgeHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "fridge_db";
    private static final int DB_VERSION = 3;
    private static SQLiteDatabase db;

    public FridgeHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createFoodManagementTab(db);
        createShoppingListTab(db);
        createScheduleTab(db);
        createRecipeCollectionTab(db);
        cTestData("shopping_list", "name", "測試", "category", "測試類", "quantity", 1);
        for (int i = 0; i < 5; i++) {
            cTestData("refrigerator", "name", "牛排", "img", "牛排照片", "category", "肉類", "quantity", 2,
                    "saving_day", 7, "expiration", "2024-06-30");
        }
        for (int i = 0; i < 4; i++) {
            cTestData("refrigerator", "name", "鱸魚", "img", "鱸魚照片", "category", "魚肉類", "quantity", 2,
                    "saving_day", 5, "expiration", "2024-06-28");
        }
        for (int i = 0; i < 6; i++) {
            cTestData("refrigerator", "name", "蛋", "img", "蛋照片", "category", "蛋豆類", "quantity", 3,
                    "saving_day", 7, "expiration", "2024-06-30");
        }
        for (int i = 0; i < 2; i++) {
            cTestData("refrigerator", "name", "高麗菜", "img", "高麗菜照片", "category", "蔬菜類", "quantity", 2,
                    "saving_day", 7, "expiration", "2024-06-30");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createShoppingListTab(SQLiteDatabase db) {
        String sql = "create table if not exists shopping_list (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(100) NOT null, " +
                "category VARCHAR(100) NOT null," +
                "quantity INTEGER NOT null)";

        db.execSQL(sql);
    }

    private void createFoodManagementTab(SQLiteDatabase db) {
        FridgeHelper.db = db;
        String sql = "create table if not exists refrigerator (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(100) NOT NULL, " +
                "img VARCHAR(100) NOT NULL, " +
                "category VARCHAR(100) NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "saving_day INTEGER NOT NULL, " +
                "expiration VARCHAR(100) NOT NULL, " +
                "expired INTEGER DEFAULT 0)";
        db.execSQL(sql);
    }

    private void createScheduleTab(SQLiteDatabase db) {
        String sql = "create table if not exists schedule (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "r_id INTEGER Not Null, " +
                "day INTEGER Not Null," +
                "cooking_date text not null)";

        db.execSQL(sql);
    }

    private void createRecipeCollectionTab(SQLiteDatabase db) {
        String sql = "create table if not exists recipe_collection (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "r_id INTEGER Not Null )";

        db.execSQL(sql);
    }

    private void cTestData(String tabName, Object... args) {
        ContentValues values = new ContentValues();
        for (int i = 0; i < args.length; i += 2) {
            String attrN = (String) args[i];
            Object attrV = args[i + 1];
            if (attrV instanceof String)
                values.put(attrN, (String) attrV);
            else if (attrV instanceof Integer) {
                values.put(attrN, (Integer) attrV);
            }
        }
        db.insert(tabName, null, values);
    }
}
