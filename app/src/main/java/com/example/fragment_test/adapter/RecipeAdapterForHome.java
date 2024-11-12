package com.example.fragment_test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.entity.Recipe;

import java.util.List;

public class RecipeAdapterForHome extends RecyclerView.Adapter<RecipeAdapterForHome.RecipeViewHolderForHome> {

    private List<Recipe> recipes;

    class RecipeViewHolderForHome extends RecyclerView.ViewHolder{

        ImageView recipePicture;
        TextView recipeName;

        public RecipeViewHolderForHome(@NonNull View itemView) {
            super(itemView);
            this.recipePicture = itemView.findViewById(R.id.recipe_picture);
            this.recipeName = itemView.findViewById(R.id.recipe_name);
        }
    }

    public RecipeAdapterForHome(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolderForHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new RecipeViewHolderForHome(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolderForHome holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.recipeName.setText(recipe.name);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
