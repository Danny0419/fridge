package com.example.fragment_test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.pojo.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> recipes;

    public RecipeAdapter(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView ingredientImg, ingredientName;
        private RecyclerView ingredientContainer;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImg = itemView.findViewById(R.id.ingredientImg);
            ingredientName = itemView.findViewById(R.id.ingredientName);
//            ingredientContainer = itemView.findViewById(R.id.ingredientContainer);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item_for_recommendation, parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.ingredientImg.setText(recipe.foodImg);
        holder.ingredientName.setText(recipe.foodName);
    }


    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
