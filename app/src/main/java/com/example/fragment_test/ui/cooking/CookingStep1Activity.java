package com.example.fragment_test.ui.cooking;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fragment_test.adapter.RecipeDetailIngredientAdapter;
import com.example.fragment_test.databinding.ActivityCookingStep1Binding;
import com.example.fragment_test.entity.RecipeWithScheduledId;

public class CookingStep1Activity extends AppCompatActivity {
    ActivityCookingStep1Binding activityCookingStep1Binding;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCookingStep1Binding = ActivityCookingStep1Binding.inflate(getLayoutInflater());
        setContentView(activityCookingStep1Binding.getRoot());

        Intent i = getIntent();
        RecipeWithScheduledId cookingRecipe = i.getParcelableExtra("cookingRecipe", RecipeWithScheduledId.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        activityCookingStep1Binding
                        .recipeIngredients.setLayoutManager(layoutManager);
        activityCookingStep1Binding
                .recipeIngredients.setAdapter(new RecipeDetailIngredientAdapter(cookingRecipe.recipe.ingredients));


        activityCookingStep1Binding.nextBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, CookingStep2Activity.class);
            intent.putExtra("cookingRecipe", cookingRecipe);
            startActivity(intent);
        });

        activityCookingStep1Binding.returnBtn.setOnClickListener(view -> {
            finish();
        });


    }
}