package com.example.fragment_test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.pojo.Ingredient;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private ArrayList<Ingredient> ingredients;

    public IngredientAdapter(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredientImg;
        public TextView ingredientName;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImg = itemView.findViewById(R.id.ingredientImg);
            ingredientName = itemView.findViewById(R.id.ingredientName);
        }
    }
    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item_for_recipe_recommendation, parent, false);
        return new IngredientViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
//        holder.ingredientImg.setText(ingredient.getImg());
//        holder.ingredientName.setText(ingredient.getName());
    }



    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
