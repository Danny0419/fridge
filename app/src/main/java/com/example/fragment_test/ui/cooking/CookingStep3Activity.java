package com.example.fragment_test.ui.cooking;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fragment_test.MainActivity2;
import com.example.fragment_test.databinding.ActivityCookingStep3Binding;
import com.example.fragment_test.entity.RecipeWithScheduledId;

public class CookingStep3Activity extends AppCompatActivity {
    private ActivityCookingStep3Binding activityCookingStep3Binding;
    private CookingViewModel viewModel;
    private TextView title;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCookingStep3Binding = ActivityCookingStep3Binding.inflate(getLayoutInflater());
        setContentView(activityCookingStep3Binding.getRoot());
        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory()).get(CookingViewModel.class);

        Intent intent = getIntent();
        RecipeWithScheduledId cookingRecipe = intent.getParcelableExtra("cookingRecipe", RecipeWithScheduledId.class);
        viewModel.loadSettlement(cookingRecipe);
        viewModel.getSettlements()
                        .observe(this, strings -> {
                            activityCookingStep3Binding.settlementsList
                                    .setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strings));
                        });

        activityCookingStep3Binding.returnBtn.setOnClickListener(view -> {
            finish();
        });

        activityCookingStep3Binding.finishBtn.setOnClickListener(view -> {
            viewModel.cooking(cookingRecipe);
            Intent intent1 = new Intent(this, MainActivity2.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
        });

        //未測試
//        title=findViewById(R.id.stepTitle);
//        title.setText("食材消耗確認");
    }
}