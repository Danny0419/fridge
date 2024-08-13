package com.example.fragment_test;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.fragment_test.databinding.ActivityRecipeDetailBinding;
import com.example.fragment_test.databinding.RecipeIntroductionBinding;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.ui.recipe.RecipeViewModel;

public class RecipeDetailActivity extends AppCompatActivity {

    private ActivityRecipeDetailBinding activityRecipeDetailBinding;
    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityRecipeDetailBinding  = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        setContentView(activityRecipeDetailBinding.getRoot());

        recipeViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RecipeViewModel.class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        Recipe recipe;
        if (intent != null) {
            recipe = intent.getParcelableExtra("recipe");
        } else {
            recipe = null;
        }
        RecipeIntroductionBinding recipeIntroduction = activityRecipeDetailBinding.recipeIntroduction;
        recipeIntroduction.recipeName.setText(recipe.name);
        recipeIntroduction.recipeImg.setText(recipe.img);
        recipeIntroduction.recipeServing.setText(Integer.toString(recipe.serving));
        activityRecipeDetailBinding.addBnt.setOnClickListener(view -> {
            recipeViewModel.addInterestingRecipe(recipe);
            finish();
        });
    }
}