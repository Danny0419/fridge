package com.example.fragment_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fragment_test.constant.IngredientCategory;
import com.example.fragment_test.fragments.FoodManagementFragment;
import com.example.fragment_test.fragments.RecipeFragment;
import com.example.fragment_test.fragments.ScheduleFragment;
import com.example.fragment_test.fragments.ShoppingListFragment;
import com.example.fragment_test.helper.FridgeHelper;
import com.example.fragment_test.pojo.RefrigeratorIngredient;
import com.example.fragment_test.pojo.RefrigeratorMap;
import com.example.fragment_test.pojo.ShoppingIngredient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavbar;
    FragmentManager supportFragmentManager = this.getSupportFragmentManager();
    private FridgeHelper fridgeHelper = new FridgeHelper(this);
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar_main));
        getSupportActionBar();
        bottomNavbar = findViewById(R.id.bottomNavBar);
        db = fridgeHelper.getReadableDatabase();
        initialize();


        bottomNavbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int onClickItemId = menuItem.getItemId();

                if (onClickItemId == R.id.home) {
                    return true;
                } else if (onClickItemId == R.id.manage) {
                    RefrigeratorMap.resetRefrigerator();
                    Cursor query = db.query("refrigerator", new String[]{"id", "name", "img", "category", "quantity", "expiration"}, "expired = 0", null, null, null, null);

                    while (query.moveToNext()) {
                        int id = query.getInt(0);
                        String name = query.getString(1);
                        String img = query.getString(2);
                        String category = query.getString(3);
                        int quantity = query.getInt(4);
                        String expiration = query.getString(5);
                        RefrigeratorIngredient refrigeratorIngredient = new RefrigeratorIngredient(id, name, img, quantity, category, 5, expiration);
                        sortIngredients(refrigeratorIngredient);
                    }

                    loadPage(new FoodManagementFragment(RefrigeratorMap.map));
                    return true;
                } else if (onClickItemId == R.id.recipe) {
                    loadPage(new RecipeFragment());
                    return true;
                } else if (onClickItemId == R.id.schedule) {
                    loadPage(new ScheduleFragment());
                    return true;
                } else if (onClickItemId == R.id.shoppingList) {
                    Cursor query = db.query("shopping_list", new String[]{"id", "name", "category", "quantity"}, null, null, null, null, null);
                    ArrayList<ShoppingIngredient> shopping_list = new ArrayList<>();

                    int id = 1;
                    String name = "";
                    String category = "";
                    int quantity = 0;
                    while (query.moveToNext()) {
                        name = query.getString(1);
                        category = query.getString(2);
                        quantity = query.getInt(3);
                        shopping_list.add(new ShoppingIngredient(id, name, category, quantity));
                        id++;
                    }
                    query.close();
                    loadShoppingListPage(shopping_list);
                    return true;
                }
                return false;
            }
        });
    }

    private void initialize() {
        Cursor query = db.query("refrigerator", new String[]{"id", "name", "img", "category", "quantity", "expiration"}, null, null, null, null, null);

        while (query.moveToNext()) {
            int id = query.getInt(0);
            String name = query.getString(1);
            String img = query.getString(2);
            String category = query.getString(3);
            int quantity = query.getInt(4);
            String expiration = query.getString(5);
            RefrigeratorIngredient refrigeratorIngredient = new RefrigeratorIngredient(id, name, img, quantity, category, 5, expiration);
            sortIngredients(refrigeratorIngredient);
        }
        query.close();
        supportFragmentManager.beginTransaction()
                .add(R.id.mainContent, new FoodManagementFragment(RefrigeratorMap.map))
                .commit();
    }
    private void sortIngredients(RefrigeratorIngredient ingredient) {
        String category = ingredient.sort;
        if (category.contains("魚肉"))
            RefrigeratorMap.map.get(IngredientCategory.FISH.name).add(ingredient);
        else if (category.contains("肉"))
            RefrigeratorMap.map.get(IngredientCategory.MEAT.name).add(ingredient);
        else if (category.contains("蛋豆"))
            RefrigeratorMap.map.get(IngredientCategory.BEAN.name).add(ingredient);
        else if (category.contains("蔬菜"))
            RefrigeratorMap.map.get(IngredientCategory.VEGETABLE.name).add(ingredient);
    }
    private void loadShoppingListPage(ArrayList<ShoppingIngredient> shoppingList) {
        ShoppingListFragment shoppingListFragment = new ShoppingListFragment();
        shoppingListFragment.setShoppingList(shoppingList);
        loadPage(shoppingListFragment);
    }
    private void loadPage(Fragment fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.mainContent, fragment)
                .commit();
    }

}