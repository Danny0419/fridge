package com.example.fragment_test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.entity.Ingredient;

import java.util.List;

public class RecipeDetailIngredientAdapter extends RecyclerView.Adapter<RecipeDetailIngredientAdapter.RecipeDetailIngredientViewHolder> {
    private List<? extends Ingredient> ingredients;

    class RecipeDetailIngredientViewHolder extends RecyclerView.ViewHolder {
        private TextView ingredientName;
        private TextView ingredientQuantity;

        public RecipeDetailIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
            ingredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
        }
    }

    public RecipeDetailIngredientAdapter(List<? extends Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public RecipeDetailIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_ingredients, parent, false);
        return new RecipeDetailIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailIngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.ingredientName.setText(ingredient.name);
        holder.ingredientQuantity.setText(Integer.toString(ingredient.quantity));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
