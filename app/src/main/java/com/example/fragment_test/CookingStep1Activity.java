package com.example.fragment_test;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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



        activityCookingStep1Binding.nextBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, CookingStep2Activity.class);
            intent.putExtra("cookingRecipe", getIntent().getParcelableExtra("cookingRecipe", RecipeWithScheduledId.class));
            startActivity(intent);
        });

        activityCookingStep1Binding.returnBtn.setOnClickListener(view -> {
            finish();
        });


    }
}