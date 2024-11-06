package com.example.fragment_test.ui.cooking;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.RecipeDetailIngredientAdapter;
import com.example.fragment_test.databinding.ActivityStartCookingBinding;
import com.example.fragment_test.databinding.RecipeIntroductionBinding;
import com.example.fragment_test.entity.RecipeWithScheduledId;
import com.example.fragment_test.ui.recipe.RecipeViewModel;

import java.util.Optional;

public class StartCookingActivity extends AppCompatActivity {
    private ActivityStartCookingBinding activityStartCookingBinding;
    private RecipeViewModel recipeViewModel;
    private CookingViewModel cookingViewModel;
    RecipeWithScheduledId scheduleRecipe;
    Intent intent;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStartCookingBinding = ActivityStartCookingBinding.inflate(getLayoutInflater());
        setContentView(activityStartCookingBinding.getRoot());
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication()));
        recipeViewModel = viewModelProvider.get(RecipeViewModel.class);
        cookingViewModel = viewModelProvider.get(CookingViewModel.class);

        intent = getIntent();
        Optional<Bundle> extras = Optional.ofNullable(intent.getExtras());
        Bundle bundle = extras.orElseThrow(RuntimeException::new);
        Optional<RecipeWithScheduledId> recipeOptional = Optional.ofNullable(bundle.getParcelable("scheduleRecipe", RecipeWithScheduledId.class));
        scheduleRecipe = recipeOptional.orElseThrow(RuntimeException::new);


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

        RecipeIntroductionBinding recipeIntroduction = activityStartCookingBinding.recipeIntroduction;
        recipeIntroduction.recipeName.setText(scheduleRecipe.recipe.name);
//        recipeIntroduction.recipeImg.setText(scheduleRecipe.recipe.img);
        recipeIntroduction.recipeServing.setText(Integer.toString(scheduleRecipe.recipe.serving));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recipeIntroduction.recipeIngredients.setLayoutManager(gridLayoutManager);
        recipeIntroduction.recipeIngredients.setAdapter(new RecipeDetailIngredientAdapter(scheduleRecipe.recipe.ingredients));

        recipeIntroduction.collectBnt.setOnClickListener(view -> {
            recipeViewModel.collectAndUnCollectRecipe(scheduleRecipe.recipe);
        });

        activityStartCookingBinding.cookingBnt.setOnClickListener(view -> {
            cookingViewModel.checkIfIngredientsSufficient(scheduleRecipe);
        });

        cookingViewModel.getAreIngredientSufficient()
                .observe(this, aBoolean -> {
                    if (aBoolean) {
                        cookingViewModel.getAreIngredientSufficient().setValue(false);
                        Intent intent = new Intent(this, CookingStep1Activity.class);
                        intent.putExtra("cookingRecipe", scheduleRecipe);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "您的食材不夠", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}