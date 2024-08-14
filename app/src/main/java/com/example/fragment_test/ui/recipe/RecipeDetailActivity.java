package com.example.fragment_test.ui.recipe;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.fragment_test.R;
import com.example.fragment_test.databinding.ActivityRecipeDetailBinding;
import com.example.fragment_test.databinding.RecipeIntroductionBinding;
import com.example.fragment_test.entity.Recipe;

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

        Bundle bundle = getIntent().getExtras();
        Recipe recipe;
        if (bundle != null) {
            recipe = bundle.getParcelable("recipe");
        } else {
            recipe = null;
        }
        RecipeIntroductionBinding recipeIntroduction = activityRecipeDetailBinding.recipeIntroduction;
        recipeIntroduction.recipeName.setText(recipe.name);
        recipeIntroduction.recipeImg.setText(recipe.img);
        recipeIntroduction.recipeServing.setText(Integer.toString(recipe.serving));

        recipeIntroduction.collectBnt.setOnClickListener(view -> {
            recipeViewModel.collectAndUnCollectRecipe(recipe);
        });

        activityRecipeDetailBinding.addBnt.setOnClickListener(view -> {
            recipeViewModel.addInterestingRecipe(recipe);
            finish();
        });
    }
}