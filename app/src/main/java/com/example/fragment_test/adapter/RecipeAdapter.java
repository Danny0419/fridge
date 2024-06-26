package com.example.fragment_test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.pojo.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private ArrayList<Recipe> recipes;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        this.context = context;
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView recipeImg, recipeName;
        private RecyclerView ingredientContainer;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImg = itemView.findViewById(R.id.recipeImg);
            recipeName = itemView.findViewById(R.id.recipeName);
//            ingredientContainer = itemView.findViewById(R.id.ingredientContainer);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card_item, parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.recipeImg.setText(recipe.foodImg);
        holder.recipeName.setText(recipe.foodName);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//        holder.ingredientContainer.setLayoutManager(layoutManager);
//        holder.ingredientContainer.setAdapter(new IngredientAdapter(recipe.ingredients));
    }


    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
