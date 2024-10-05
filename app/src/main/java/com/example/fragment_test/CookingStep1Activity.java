package com.example.fragment_test;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fragment_test.databinding.ActivityCookingStep1Binding;
import com.example.fragment_test.entity.Step;

import java.util.ArrayList;

public class CookingStep1Activity extends AppCompatActivity {
    ActivityCookingStep1Binding activityCookingStep1Binding;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCookingStep1Binding = ActivityCookingStep1Binding.inflate(getLayoutInflater());
        setContentView(activityCookingStep1Binding.getRoot());

        Intent intent = getIntent();
        ArrayList<Step> steps = intent.getParcelableArrayListExtra("steps", Step.class);

        activityCookingStep1Binding.nextBtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, CookingStep2Activity.class);
            startActivity(intent1);
        });

        activityCookingStep1Binding.returnBtn.setOnClickListener(view -> {
            finish();
        });


    }
}