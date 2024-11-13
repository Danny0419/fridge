package com.example.fragment_test.ui.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.MealsPrepareScheduleEachRecipeAdapter;
import com.example.fragment_test.adapter.SchedulePreparedRecipeAdapter;
import com.example.fragment_test.databinding.ActivityMealsPrepareDialogBinding;

import java.time.DayOfWeek;

public class MealsPrepareDialogActivity extends AppCompatActivity {

    ActivityMealsPrepareDialogBinding mealsPrepareDialogBinding;
    MealsPreparedViewModel viewModel;
    int date;
    int dayOfWeekInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mealsPrepareDialogBinding = ActivityMealsPrepareDialogBinding.inflate(getLayoutInflater());
        setContentView(mealsPrepareDialogBinding.getRoot());

        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MealsPreparedViewModel.class);

        viewModel.getScheduledRecipes()
                .observe(this, (recipes) -> {
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
                    RecyclerView foodItem = mealsPrepareDialogBinding.scheduleItem.foodItem;
                    foodItem.setLayoutManager(gridLayoutManager);
                    MealsPrepareScheduleEachRecipeAdapter scheduleEachRecipeAdapter = new MealsPrepareScheduleEachRecipeAdapter(recipes, date);
                    scheduleEachRecipeAdapter.setOnClickListener((date, recipe) -> {
                        viewModel.unSchedule(date, recipe);
                    });
                    foodItem.setAdapter(scheduleEachRecipeAdapter);
                });

        viewModel.getPreparedRecipes()
                .observe(this, (recipes -> {
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
                    RecyclerView foodPrepareItem = mealsPrepareDialogBinding.foodPrepareItem;
                    foodPrepareItem.setLayoutManager(gridLayoutManager);
                    SchedulePreparedRecipeAdapter schedulePreparedRecipeAdapter = new SchedulePreparedRecipeAdapter(recipes);
                    schedulePreparedRecipeAdapter.setOnclickListener(recipe -> viewModel.schedule(date, dayOfWeekInt, recipe));
                    foodPrepareItem.setAdapter(schedulePreparedRecipeAdapter);
                }));

        Button invisibleBtn = findViewById(R.id.select_meal_btn);
        invisibleBtn.setVisibility(View.GONE);

        // toolbar setting
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.recipe_detail_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // 隐藏預設返回按鈕
        }

        // 點擊返回
        ImageView customBackButton = findViewById(R.id.close_view_btn);
        customBackButton.setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        date = intent.getIntExtra("date", 0);
        dayOfWeekInt = DayOfWeek.valueOf(intent.getStringExtra("dayOfWeek")).getValue();

        String dayOfWeek = intent.getStringExtra("dayOfWeek");
//        mealsPrepareDialogBinding.scheduleItem.eachDayText.setText(dayOfWeek);

        viewModel.loadSchedules(date);
        viewModel.loadPreparedRecipes();

    }
}