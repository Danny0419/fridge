package com.example.fragment_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.fragment_test.fragments.FoodManagementFragment;
import com.example.fragment_test.fragments.RecipeFragment;
import com.example.fragment_test.fragments.TestFragment;
import com.example.fragment_test.pojo.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private ArrayList<Ingredient> ingredient;
    private Fragment testFragment, recipeFragment, foodManagementFragment, mainPageTitleFragment;
    private int curPage = 3;
    private MyAdapter myAdapter;
    FragmentManager supportFragmentManager = this.getSupportFragmentManager();

    private Map<String, List<Ingredient>> allIngredient = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = openOrCreateDatabase("fridge_sys", Context.MODE_PRIVATE, null);
        ImageButton addFoodButton = findViewById(R.id.addFoodButton);
        ImageButton recipeButton = findViewById(R.id.recipeButton);
        ImageButton foodManagementButton = findViewById(R.id.foodManagementButton);

//        String sql = "create table  if not exists food(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,name text,sort text,quantity text,state INTEGER default 0)";
//        database.execSQL(sql);
//        sql = "insert into food values(null,'吳郭魚','魚肉類','3尾',0),(null,'吳郭魚','魚肉類','3尾',0),(null,'沙魚','魚肉類','4尾',0),(null,'吳郭魚','魚肉類','3尾',1),(null,'吳郭魚','魚肉類','3尾',1),(null,'吳郭魚','魚肉類','3尾',0),(null,'吳郭魚','魚肉類','3尾',0),(null,'吳郭魚','魚肉類','3尾',0),(null,'吳郭魚','魚肉類','3尾',0),(null,'吳郭魚','魚肉類','3尾',0)";
//        database.execSQL(sql);

        initMainPage();

        foodManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRefrigerator();
            }
        });

        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredient = getMyFood();
                if (curPage == 3) {
                    ((TestFragment) testFragment).reFreshUI(ingredient);
                    return;
                }

                loadAddFoodUI();
            }
        });

        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRecipeUI();
            }
        });
    }

    public void initMainPage() {
        mainPageTitleFragment = new MainPageTitleFragment();
        foodManagementFragment = new FoodManagementFragment();
        supportFragmentManager.beginTransaction()
                .add(R.id.titleContent, mainPageTitleFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
        supportFragmentManager.beginTransaction()
                .add(R.id.mainContent, foodManagementFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    public void loadRefrigerator() {
        curPage = 2;

        supportFragmentManager.beginTransaction()
                .replace(R.id.mainContent, foodManagementFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    public void loadAddFoodUI() {
        curPage = 3;

        ingredient = getMyFood();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("food", ingredient);

        testFragment = new TestFragment();
        testFragment.setArguments(bundle);


        supportFragmentManager.beginTransaction()
                .replace(R.id.mainContent, testFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
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

    public void loadRecipeUI() {
        curPage = 1;
        recipeFragment = new RecipeFragment();
        supportFragmentManager.beginTransaction()
                .replace(R.id.mainContent, recipeFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

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