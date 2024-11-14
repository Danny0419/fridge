package com.example.fragment_test.ui.cooking;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fragment_test.databinding.ActivityCookingStep2Binding;
import com.example.fragment_test.entity.RecipeWithScheduledId;

public class CookingStep2Activity extends AppCompatActivity {
    ActivityCookingStep2Binding activityCookingStep2Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCookingStep2Binding = ActivityCookingStep2Binding.inflate(getLayoutInflater());
        setContentView(activityCookingStep2Binding.getRoot());


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