package com.example.fragment_test;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fragment_test.databinding.ActivityRecipeDetailBinding;
import com.example.fragment_test.databinding.RecipeIntroductionBinding;
import com.example.fragment_test.entity.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {

    private ActivityRecipeDetailBinding activityRecipeDetailBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityRecipeDetailBinding  = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        setContentView(activityRecipeDetailBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        Recipe recipe = null;
        if (intent != null) {
            recipe = intent.getParcelableExtra("recipe");
        }
        RecipeIntroductionBinding recipeIntroduction = activityRecipeDetailBinding.recipeIntroduction;
        recipeIntroduction.recipeName.setText(recipe.name);
    }
}