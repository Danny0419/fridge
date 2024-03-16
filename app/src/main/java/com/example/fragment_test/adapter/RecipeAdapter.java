package com.example.fragment_test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.MainActivity;
import com.example.fragment_test.MyAdapter;
import com.example.fragment_test.R;
import com.example.fragment_test.pojo.Day;
import com.example.fragment_test.pojo.Recipe;

import java.util.ArrayList;
import java.util.Map;

public class RecipeAdapter extends RecyclerView.Adapter {
    private ArrayList<Recipe> recipes;
    private Day[] days;


    public RecipeAdapter(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        days = Day.values();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        public TextView foodImg;
        public TextView foodName;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImg = itemView.findViewById(R.id.foodImg);
            foodName = itemView.findViewById(R.id.foodName);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_food_container, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RecipeViewHolder)holder).foodImg.setText(recipes.get(position).getFoodImg());
        ((RecipeViewHolder)holder).foodName.setText(recipes.get(position).getFoodName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
