package com.example.fragment_test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class CookingFragment extends Fragment {

    private ViewStub recipeIntroductionStub, recipeStepsStub, ingredientConsumptionConfirmStub;
    private View recipeIntroduction, recipeSteps, ingredientConsumptionConfirm;
    private Button returnBtn, nextBtn, finishBtn;
    private int currentLayout = 1;  //現在在哪個頁面

    public CookingFragment() {
        // Required empty public constructor
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cooking, container, false);

        recipeIntroductionStub = rootView.findViewById(R.id.recipe_introduction);
        recipeStepsStub = rootView.findViewById(R.id.recipe_steps);
        ingredientConsumptionConfirmStub = rootView.findViewById(R.id.ingredient_consumption);

        recipeIntroduction = recipeIntroductionStub.inflate();
        returnBtn = rootView.findViewById(R.id.return_btn);
        nextBtn = rootView.findViewById(R.id.next_btn);
        finishBtn = rootView.findViewById(R.id.finish_btn);

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
                // Close the fragment or finish activity
                getActivity().finish();
            }
        });

        return rootView;
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
