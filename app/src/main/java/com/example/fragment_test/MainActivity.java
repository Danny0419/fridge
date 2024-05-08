package com.example.fragment_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fragment_test.adapter.MyAdapter;
import com.example.fragment_test.fragments.FoodManagementFragment;
import com.example.fragment_test.fragments.RecipeFragment;
import com.example.fragment_test.fragments.ScheduleFragment;
import com.example.fragment_test.fragments.ShoppingListFragment;
import com.example.fragment_test.pojo.Ingredient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavbar;
    private SQLiteDatabase database;
    private ArrayList<Ingredient> shoppingList = new ArrayList<>();
    private Fragment curFragment;
    private int curPage = 3;
    private MyAdapter myAdapter;
    FragmentManager supportFragmentManager = this.getSupportFragmentManager();

    private static final int FRIDGE_PAGE = 2;
    private static final int ADD_FOOD_PAGE = 3;
    private static final int RECIPE_PAGE = 1;

    private Map<String, List<Ingredient>> allIngredient = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        bottomNavbar = findViewById(R.id.bottomNavBar);

        bottomNavbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int onClickItemId = menuItem.getItemId();

                if (onClickItemId == R.id.home) {
                    return true;
                } else if (onClickItemId == R.id.manage) {
                    loadPage(new FoodManagementFragment());
                    return true;
                } else if (onClickItemId == R.id.recipe) {
                    loadPage(new RecipeFragment());
                    return true;
                } else if (onClickItemId == R.id.schedule) {
                    loadPage(new ScheduleFragment());
                    return true;
                } else if (onClickItemId == R.id.shoppingList) {
                    shoppingList.add(new Ingredient(0, "吳郭魚", "無" ,"魚類", "2", 0));
                    loadShoppingListPage();
                    return true;
                }
                return false;
            }
        });
    }

    private void initialize(){
        supportFragmentManager.beginTransaction()
                .add(R.id.mainContent, new FoodManagementFragment())
                .commit();
    }

    private void loadShoppingListPage(){
        ShoppingListFragment shoppingListFragment = new ShoppingListFragment();
        shoppingListFragment.setShoppingList(shoppingList);
        loadPage(shoppingListFragment);
    }

    private void loadPage(Fragment fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.mainContent, fragment)
                .commit();
    }


    public ArrayList<Ingredient> getMyFood() {
        Cursor cursor = database.rawQuery("select * from food", null);
        ArrayList<Ingredient> ingredient = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String sort = cursor.getString(2);
            String quantity = cursor.getString(3);
            int state = cursor.getInt(4);
            ingredient.add(new Ingredient(id, name, "沒有", sort, quantity, state));
        }
        return ingredient;
    }

    public ArrayList<String> getAllKindsOfFood() {
        String sql = "select distinct sort from refrigerator";
        Cursor cursor = database.rawQuery(sql, null);
        ArrayList<String> kindsOfFood = new ArrayList<>();

        while (cursor.moveToNext()) {
            String kind = cursor.getString(0);
            kindsOfFood.add(kind);
        }
        return kindsOfFood;
    }

    public ArrayList<Ingredient> getAllIngredient() {
        return null;
    }

}