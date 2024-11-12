package com.example.fragment_test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.entity.RecipeIngredient;

import java.util.List;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.RecipeIngredientViewHolder> {

    List<RecipeIngredient> ingredients;

    public RecipeIngredientAdapter(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {

        public ImageView ingredientImg;
        public TextView ingredientName;

        public RecipeIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImg = itemView.findViewById(R.id.ingredientImg);
            ingredientName = itemView.findViewById(R.id.ingredientName);
        }
    }

    @NonNull
    @Override
    public RecipeIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredient_card_item, parent, false);
        return new RecipeIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientViewHolder holder, int position) {
//        holder.ingredientImg(ingredients.get(position).img);
        holder.ingredientName.setText(ingredients.get(position).name);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
