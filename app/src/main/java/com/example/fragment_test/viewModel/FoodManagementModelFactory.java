package com.example.fragment_test.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FoodManagementModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Context context;

    public FoodManagementModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FoodManagementViewModel(context);
    }
}
