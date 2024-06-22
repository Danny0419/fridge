package com.example.fragment_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fragment_test.adapter.MyAdapter;
import com.example.fragment_test.fragments.FoodManagementFragment;
import com.example.fragment_test.fragments.RecipeFragment;
import com.example.fragment_test.fragments.ScheduleFragment;
import com.example.fragment_test.fragments.ShoppingListFragment;
import com.example.fragment_test.helper.FridgeHelper;
import com.example.fragment_test.pojo.Ingredient;
import com.example.fragment_test.pojo.ShoppingListBean;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavbar;
    FragmentManager supportFragmentManager = this.getSupportFragmentManager();
    private FridgeHelper fridgeHelper = new FridgeHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar_main));
        getSupportActionBar();
        initialize();

        bottomNavbar = findViewById(R.id.bottomNavBar);
        SQLiteDatabase db = fridgeHelper.getReadableDatabase();
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
                    Cursor query = db.query("shopping_list", new String[]{"id", "name", "category", "quantity"}, null, null, null, null, null);
                    ArrayList<ShoppingListBean> shopping_list = new ArrayList<>();

                    int id = 1;
                    String name = "";
                    String category = "";
                    int quantity = 0;
                    while (query.moveToNext()) {
                        name = query.getString(1);
                        category = query.getString(2);
                        quantity = query.getInt(3);
                        shopping_list.add(new ShoppingListBean(id, name, category, quantity));
                        id++;
                    }
                    loadShoppingListPage(shopping_list);
                    return true;
                }
                return false;
            }
        });
    }

    private void initialize() {
        supportFragmentManager.beginTransaction()
                .add(R.id.mainContent, new FoodManagementFragment())
                .commit();
    }

    private void loadShoppingListPage(ArrayList<ShoppingListBean> shoppingList) {
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