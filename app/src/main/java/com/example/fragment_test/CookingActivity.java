package com.example.fragment_test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CookingActivity extends AppCompatActivity {

    private ViewStub recipeIntroductionStub, recipeStepsStub, ingredientConsumptionConfirmStub;
    private View recipeIntroduction, recipeSteps, ingredientConsumptionConfirm;
    private Button returnBtn, nextBtn, finishBtn;
    private int currentLayout = 1;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipeIntroductionStub = findViewById(R.id.recipe_introduction);
        recipeStepsStub = findViewById(R.id.recipe_steps);
        ingredientConsumptionConfirmStub = findViewById(R.id.ingredient_consumption);

        recipeIntroduction = recipeIntroductionStub.inflate();
        returnBtn = findViewById(R.id.return_btn);
        nextBtn = findViewById(R.id.next_btn);
        finishBtn = findViewById(R.id.finish_btn);

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousLayout();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextLayout();
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity
            }
        });
    }

    private void showPreviousLayout() {
        switch (currentLayout) {
            case 2:
                recipeSteps.setVisibility(View.GONE);
                recipeIntroduction.setVisibility(View.VISIBLE);
                currentLayout = 1;
                break;
            case 3:
                ingredientConsumptionConfirm.setVisibility(View.GONE);
                recipeSteps.setVisibility(View.VISIBLE);
                currentLayout = 2;
                break;
        }
    }

    private void showNextLayout() {
        switch (currentLayout) {
            case 1:
                recipeIntroduction.setVisibility(View.GONE);
                if (recipeSteps == null) {
                    recipeSteps = recipeStepsStub.inflate(); // Inflate layout B if not already done
                } else {
                    recipeSteps.setVisibility(View.VISIBLE);
                }
                currentLayout = 2;
                break;
            case 2:
                recipeSteps.setVisibility(View.GONE);
                if (ingredientConsumptionConfirm == null) {
                    ingredientConsumptionConfirm = ingredientConsumptionConfirmStub.inflate(); // Inflate layout C if not already done
                } else {
                    ingredientConsumptionConfirm.setVisibility(View.VISIBLE);
                }
                currentLayout = 3;
                break;
        }
    }
}