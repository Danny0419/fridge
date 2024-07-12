package com.example.fragment_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fragment_test.database.RefrigeratorDAO;
import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.ui.refrigerator.FoodManagementFragment;
import com.example.fragment_test.ui.recipe.RecipeFragment;
import com.example.fragment_test.ui.schedule.ScheduleFragment;
import com.example.fragment_test.ui.shopping_list.ShoppingListFragmentOld;
import com.example.fragment_test.helper.FridgeHelper;
import com.example.fragment_test.entity.ShoppingIngredient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavbar;
    FragmentManager supportFragmentManager = this.getSupportFragmentManager();
    private FridgeHelper fridgeHelper = new FridgeHelper(this);
    SQLiteDatabase db;

    RefrigeratorDAO refrigeratorDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar_main));
        getSupportActionBar();
        bottomNavbar = findViewById(R.id.bottomNavBar);
//        db = fridgeHelper.getReadableDatabase();
        FridgeDatabase fridgeDb = FridgeDatabase.getInstance(this);
        refrigeratorDAO = fridgeDb.refrigeratorDAO();
        initialize();


        bottomNavbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int onClickItemId = menuItem.getItemId();

                if (onClickItemId == R.id.home) {
                    return true;
                } else if (onClickItemId == R.id.manage) {
                    loadPage(new FoodManagementFragment());
//                    RefrigeratorMap.resetRefrigerator();
//                    Maybe.fromCallable(new Callable<List<RefrigeratorIngredient>>() {
//                                @Override
//                                public List<RefrigeratorIngredient> call() throws Exception {
//                                    return refrigeratorDAO.getAllRefrigeratorIngredients();
//                                }
//                            })
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new DisposableMaybeObserver<List<RefrigeratorIngredient>>() {
//                                @Override
//                                public void onSuccess(List<RefrigeratorIngredient> refrigeratorIngredients) {
//                                    List<RefrigeratorIngredient> query = refrigeratorIngredients;
//                                    for (RefrigeratorIngredient value :
//                                            query) {
//                                        sortIngredients(value);
//                                    }
//                                    loadPage(new FoodManagementFragment(RefrigeratorMap.map));
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    Log.e("subscribe",e.getMessage());
//                                }
//
//                                @Override
//                                public void onComplete() {
//                                    loadPage(new FoodManagementFragment(RefrigeratorMap.map));
//                                }
//                            });
                    return true;
                } else if (onClickItemId == R.id.recipe) {
                    loadPage(new RecipeFragment());
                    return true;
                } else if (onClickItemId == R.id.schedule) {
                    loadPage(new ScheduleFragment());
                    return true;
                } else if (onClickItemId == R.id.shoppingList) {
//                    Cursor query = db.query("shopping_list", new String[]{"id", "name", "category", "quantity"}, null, null, null, null, null);
//                    ArrayList<ShoppingIngredient> shopping_list = new ArrayList<>();
//
//                    int id = 1;
//                    String name = "";
//                    String category = "";
//                    int quantity = 0;
//                    while (query.moveToNext()) {
//                        name = query.getString(1);
//                        category = query.getString(2);
//                        quantity = query.getInt(3);
//                        shopping_list.add(new ShoppingIngredient(id, name, category, quantity));
//                        id++;
//                    }
//                    query.close();
//                    loadShoppingListPage(shopping_list);
                    return true;
                }
                return false;
            }
        });
    }

    private void initialize() {
        loadPage(new FoodManagementFragment());
//        Maybe.fromCallable(new Callable<List<RefrigeratorIngredient>>() {
//                    @Override
//                    public List<RefrigeratorIngredient> call() throws Exception {
//                        refrigeratorDAO.addIngredients();
//                        return null;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableMaybeObserver<List<RefrigeratorIngredient>>() {
//                    @Override
//                    public void onSuccess(List<RefrigeratorIngredient> refrigeratorIngredients) {
////                        List<RefrigeratorIngredient> query = refrigeratorIngredients;
////                        for (RefrigeratorIngredient value :
////                                query) {
////                            sortIngredients(value);
////                        }
//                        supportFragmentManager.beginTransaction()
//                                .add(R.id.mainContent, new FoodManagementFragment(RefrigeratorMap.map))
//                                .commit();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        supportFragmentManager.beginTransaction()
//                                .add(R.id.mainContent, new FoodManagementFragment(RefrigeratorMap.map))
//                                .commit();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        supportFragmentManager.beginTransaction()
//                                .add(R.id.mainContent, new FoodManagementFragment(RefrigeratorMap.map))
//                                .commit();
//                    }
//                });
//        Cursor query = db.query("refrigerator", new String[]{"id", "name", "img", "category", "quantity", "expiration"}, null, null, null, null, null);

//        for (RefrigeratorIngredient value :
//                query) {
//            sortIngredients(value);
//        }
//        while (query.moveToNext()) {
//            int id = query.getInt(0);
//            String name = query.getString(1);
//            String img = query.getString(2);
//            String category = query.getString(3);
//            int quantity = query.getInt(4);
//            String expiration = query.getString(5);
//            RefrigeratorIngredient refrigeratorIngredient = new RefrigeratorIngredient(id, name, img, quantity, category, 5, expiration);
//            sortIngredients(refrigeratorIngredient);
//        }
//        query.close();

    }

    private void loadShoppingListPage(ArrayList<ShoppingIngredient> shoppingList) {
        ShoppingListFragmentOld shoppingListFragment = new ShoppingListFragmentOld();
        shoppingListFragment.setShoppingList(shoppingList);
        loadPage(shoppingListFragment);
    }

    private void loadPage(Fragment fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.mainContent, fragment)
                .commit();
    }

}