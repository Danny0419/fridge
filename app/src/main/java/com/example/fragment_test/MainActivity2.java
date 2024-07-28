package com.example.fragment_test;

import android.os.Bundle;

import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.repository.RefrigeratorIngredientRepository;
import com.example.fragment_test.ui.refrigerator.FoodManagementViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fragment_test.databinding.ActivityMain2Binding;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        List<RefrigeratorIngredient> refrigeratorIngredients = List.of(
//                new RefrigeratorIngredient("牛小排", "肉類", 2, "牛小排照片", 7, "2024-7-28", 0),
//                new RefrigeratorIngredient("豬排", "肉類", 2, "豬排照片", 7, "2024-7-28", 0),
//                new RefrigeratorIngredient("牛小排", "肉類", 2, "牛小排照片", 7, "2024-7-28", 0),
//                new RefrigeratorIngredient("高麗菜", "蔬菜類", 2, "高麗菜照片", 7, "2024-7-28", 0),
//                new RefrigeratorIngredient("豆腐", "蛋豆類", 2, "豆腐照片", 7, "2024-7-28", 0)
//        );

//        produceRefrigeratorFakeData(new FoodManagementViewModel(getApplication()), refrigeratorIngredients);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_refrigerator, R.id.navigation_recipe,
                R.id.navigation_schedule, R.id.navigation_shopping_list, R.id.navigation_camera)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        Toolbar toolbar = binding.toolbarMain;
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        setSupportActionBar(toolbar);


    }

    private void produceRefrigeratorFakeData(FoodManagementViewModel viewModel, List<RefrigeratorIngredient> ingredients){
        viewModel.addRefrigeratorIngredients(ingredients);
    }

}