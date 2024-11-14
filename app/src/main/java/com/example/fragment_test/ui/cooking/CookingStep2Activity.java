package com.example.fragment_test.ui.cooking;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fragment_test.adapter.RecipeDetailIngredientAdapter;
import com.example.fragment_test.adapter.RecipeDetailStepsAdapter;
import com.example.fragment_test.databinding.ActivityCookingStep2Binding;
import com.example.fragment_test.entity.RecipeWithScheduledId;

public class CookingStep2Activity extends AppCompatActivity {
    ActivityCookingStep2Binding activityCookingStep2Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCookingStep2Binding = ActivityCookingStep2Binding.inflate(getLayoutInflater());
        setContentView(activityCookingStep2Binding.getRoot());

        Intent i = getIntent();
        RecipeWithScheduledId cookingRecipe = i.getParcelableExtra("cookingRecipe", RecipeWithScheduledId.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        activityCookingStep2Binding.steps
                .steps.setLayoutManager(layoutManager);
        activityCookingStep2Binding.steps
                .steps.setAdapter(new RecipeDetailStepsAdapter(cookingRecipe.recipe.steps));


        activityCookingStep2Binding.nextBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, CookingStep3Activity.class);
            intent.putExtra("cookingRecipe", getIntent().getParcelableExtra("cookingRecipe", RecipeWithScheduledId.class));
            startActivity(intent);
        });

        activityCookingStep2Binding.returnBtn.setOnClickListener(view -> {
            finish();
        });
    }
}