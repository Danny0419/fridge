package com.example.fragment_test.ui.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.ScheduleEachRecipeAdapter;
import com.example.fragment_test.adapter.SchedulePreparedRecipeAdapter;
import com.example.fragment_test.databinding.ActivityMealsPrepareDialogBinding;
import com.example.fragment_test.entity.Recipe;

import java.util.List;

public class MealsPrepareDialogActivity extends AppCompatActivity {

    ActivityMealsPrepareDialogBinding mealsPrepareDialogBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mealsPrepareDialogBinding = ActivityMealsPrepareDialogBinding.inflate(getLayoutInflater());
        setContentView(mealsPrepareDialogBinding.getRoot());

        Button invisibleBtn=findViewById(R.id.select_meal_btn);
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

        String dayOfWeek = intent.getStringExtra("dayOfWeek");
        TextView eachDayText = mealsPrepareDialogBinding.scheduleItem.eachDayText;
        eachDayText.setText(dayOfWeek);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        List<Recipe> scheduledRecipes = intent.getParcelableExtra("scheduledRecipes");
        RecyclerView foodItem = mealsPrepareDialogBinding.scheduleItem.foodItem;
        foodItem.setLayoutManager(gridLayoutManager);
        foodItem.setAdapter(new ScheduleEachRecipeAdapter(scheduledRecipes));

        List<Recipe> preparedRecipe = intent.getParcelableExtra("PreparedRecipe");
        RecyclerView foodPrepareItem = mealsPrepareDialogBinding.foodPrepareItem;
        foodPrepareItem.setLayoutManager(gridLayoutManager);
        foodPrepareItem.setAdapter(new SchedulePreparedRecipeAdapter(preparedRecipe));

    }
}