package com.example.fragment_test.ui.cooking;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.RecipeDetailIngredientAdapter;
import com.example.fragment_test.adapter.RecipeDetailStepsAdapter;
import com.example.fragment_test.databinding.ActivityStartCookingBinding;
import com.example.fragment_test.databinding.RecipeIntroductionBinding;
import com.example.fragment_test.databinding.RecipeStepsBinding;
import com.example.fragment_test.entity.RecipeWithScheduledId;
import com.example.fragment_test.ui.recipe.RecipeViewModel;

import java.time.LocalDate;
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

        recipeViewModel.loadRecipePic(scheduleRecipe.recipe);
        recipeViewModel.loadRecipeSteps(scheduleRecipe.recipe);

        // toolbar setting
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.recipe_detail_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // 隐藏預設返回按鈕
        }

        // 點擊返回
        ImageView customBackButton = findViewById(R.id.close_view_btn);
        customBackButton.setOnClickListener(view -> finish());

    }

    @Override
    protected void onStart() {
        super.onStart();

        RecipeIntroductionBinding recipeIntroduction = activityStartCookingBinding.recipeIntroduction;
        recipeIntroduction.recipeName.setText(scheduleRecipe.recipe.name);
//        recipeIntroduction.recipeImg.setText(scheduleRecipe.recipe.img);
        recipeIntroduction.recipeServing.setText("份量\n"+scheduleRecipe.recipe.serving+"人份");
        recipeIntroduction.collectBnt.setVisibility(View.GONE);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recipeIntroduction.recipeIngredients.setLayoutManager(gridLayoutManager);
        recipeIntroduction.recipeIngredients.setAdapter(new RecipeDetailIngredientAdapter(scheduleRecipe.recipe.ingredients));



        RecipeStepsBinding recipeStepsBinding = activityStartCookingBinding.recipeSteps;

        recipeViewModel.getRecipeDetail()
                .observe(this, recipe -> {
                    recipeIntroduction.recipeImg.setImageBitmap(recipe.pic);
                });
        recipeViewModel.getRecipeSteps()
                .observe(this, steps -> {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recipeStepsBinding.steps.setLayoutManager(layoutManager);
                    recipeStepsBinding.steps.setAdapter(new RecipeDetailStepsAdapter(steps));
                    scheduleRecipe.recipe.steps = steps;
                });

        recipeIntroduction.collectBnt.setOnClickListener(view -> {
            recipeViewModel.collectAndUnCollectRecipe(scheduleRecipe.recipe);
        });

        activityStartCookingBinding.cookingBnt.setOnClickListener(view -> {
            int value = LocalDate.now().getDayOfWeek().getValue();
            if (value != scheduleRecipe.dayOfWeek) {
                Toast.makeText(this, "請別日再來", Toast.LENGTH_SHORT).show();
                return;
            }
            cookingViewModel.checkIfIngredientsSufficient(scheduleRecipe);
            cookingViewModel.getAreIngredientSufficient()
                    .observe(this, aBoolean -> {
                        if (aBoolean) {
                            Intent intent = new Intent(this, CookingStep1Activity.class);
                            intent.putExtra("cookingRecipe", scheduleRecipe);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "您的食材不夠", Toast.LENGTH_SHORT).show();
                        }
                    });
        });


    }
}