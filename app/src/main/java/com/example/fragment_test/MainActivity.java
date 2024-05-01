package com.example.fragment_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.fragment_test.fragments.FoodManagementFragment;
import com.example.fragment_test.fragments.RecipeFragment;
import com.example.fragment_test.fragments.ShoppingListFragment;
import com.example.fragment_test.fragments.TestFragment;
import com.example.fragment_test.pojo.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private ArrayList<Ingredient> ingredient;
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
        database = openOrCreateDatabase("fridge_sys", Context.MODE_PRIVATE, null);
//        ImageButton shoppingListButton = findViewById(R.id.shoppingListButton);
//        ImageButton recipeButton = findViewById(R.id.recipeButton);
//        ImageButton foodManagementButton = findViewById(R.id.foodManagementButton);

//        initMainPage();

//        foodManagementButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadPage(FRIDGE_PAGE, new FoodManagementFragment());
//            }
//        });
//
//        shoppingListButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadShoppingListUI();
//            }
//        });
//
//        recipeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadPage(RECIPE_PAGE, new RecipeFragment());
//            }
//        });
    }

    public void initMainPage() {
        curFragment = new MainPageTitleFragment();

        supportFragmentManager.beginTransaction()
                .add(R.id.titleContent, new MainPageTitleFragment())
                .add(R.id.mainContent, new FoodManagementFragment())
                .add(R.id.navBarFrag, new NavBarFragment())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

//        curFragment = new FoodManagementFragment();
//
//        supportFragmentManager.beginTransaction()
//                .add(R.id.mainContent, curFragment)
//                .setReorderingAllowed(true)
//                .addToBackStack(null)
//                .commit();
    }

    public void loadPage(int curPage, Fragment curFragment) {
        curPage = curPage;

        this.curFragment = curFragment;

        supportFragmentManager.beginTransaction()
                .replace(R.id.mainContent, curFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    public void loadShoppingListUI() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ShoppingListActivity.class);
        startActivity(intent);
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