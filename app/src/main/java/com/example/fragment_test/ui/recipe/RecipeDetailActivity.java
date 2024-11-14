package com.example.fragment_test.ui.recipe;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.RecipeDetailIngredientAdapter;
import com.example.fragment_test.adapter.RecipeDetailStepsAdapter;
import com.example.fragment_test.databinding.ActivityRecipeDetailBinding;
import com.example.fragment_test.databinding.RecipeIntroductionBinding;
import com.example.fragment_test.databinding.RecipeStepsBinding;
import com.example.fragment_test.entity.Recipe;

import java.util.Optional;

public class RecipeDetailActivity extends AppCompatActivity {

    private ActivityRecipeDetailBinding activityRecipeDetailBinding;
    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityRecipeDetailBinding = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        setContentView(activityRecipeDetailBinding.getRoot());

        // toolbar setting
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.recipe_detail_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // 隐藏預設返回按鈕
        }

        // 點擊返回
        ImageView customBackButton = findViewById(R.id.close_view_btn);
        customBackButton.setOnClickListener(view -> onBackPressed());

        recipeViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RecipeViewModel.class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Optional<Bundle> extras = Optional.ofNullable(getIntent().getExtras());
        Bundle bundle = extras.orElseThrow(RuntimeException::new);
        Optional<Recipe> recipeOptional = Optional.ofNullable(bundle.getParcelable("recipe"));
        Recipe recipe = recipeOptional.orElseThrow(RuntimeException::new);

        recipeViewModel.loadRecipePic(recipe);
        RecipeIntroductionBinding recipeIntroduction = activityRecipeDetailBinding.recipeIntroduction;
        recipeIntroduction.recipeName.setText(recipe.name);
        recipeViewModel.getRecipeDetail()
                        .observe(this, r -> {
                            recipeIntroduction.recipeImg.setImageBitmap(recipe.pic);
                        });

        recipeIntroduction.recipeServing.setText(Integer.toString(recipe.serving));

        recipeViewModel.loadRecipeSteps(recipe);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recipeIntroduction.recipeIngredients.setLayoutManager(gridLayoutManager);
        recipeIntroduction.recipeIngredients.setAdapter(new RecipeDetailIngredientAdapter(recipe.ingredients));

        RecipeStepsBinding recipeStepsBinding = activityRecipeDetailBinding.recipeSteps;
        recipeViewModel.getRecipeSteps()
                .observe(this, steps -> {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recipeStepsBinding.recipeSteps.setLayoutManager(layoutManager);
                    recipeStepsBinding.recipeSteps.setAdapter(new RecipeDetailStepsAdapter(steps));
                });

        recipeIntroduction.collectBnt.setOnClickListener(view -> {
            recipeViewModel.collectAndUnCollectRecipe(recipe);
        });

        activityRecipeDetailBinding.addBnt.setOnClickListener(view -> {
            recipeViewModel.addInterestingRecipe(recipe);
            finish();
        });
    }
}