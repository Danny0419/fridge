package com.example.fragment_test;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fragment_test.databinding.ActivityCookingStep2Binding;

public class CookingStep2Activity extends AppCompatActivity {
    ActivityCookingStep2Binding activityCookingStep2Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCookingStep2Binding = ActivityCookingStep2Binding.inflate(getLayoutInflater());
        setContentView(activityCookingStep2Binding.getRoot());

        activityCookingStep2Binding.nextBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, CookingStep3Activity.class);
            startActivity(intent);
        });

        activityCookingStep2Binding.returnBtn.setOnClickListener(view -> {
            finish();
        });
    }
}