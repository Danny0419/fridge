package com.example.fragment_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fragment_test.RecipeRecommend.RecipeRecommendationActivity;
import com.example.fragment_test.ScannerList.OcrActivity;
import com.example.fragment_test.databinding.ActivityMain2Binding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity{

    private ActivityMain2Binding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        //Intent intent = new Intent(MainActivity2.this, OcrActivity.class);
        //startActivity(intent);


//        Button button=this.findViewById(R.id.ApiText);
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity2.this, RecipeRecommendationActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}