package com.example.fragment_test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

        public TextView ingredientImg;
        public TextView ingredientName;

        public RecipeIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
//            ingredientImg = itemView.findViewById(R.id.ingredientImg);
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
//        holder.ingredientImg.setText(ingredients.get(position).img);
        RecipeIngredient recipeIngredient = ingredients.get(position);
        holder.ingredientName.setText(" "+recipeIngredient.name+" ");

        switch (recipeIngredient.sufficient) {
            case 0:

                holder.ingredientName.setBackgroundResource(R.drawable.warn_gray_rectangle);
                holder.ingredientName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
                break;
            case 1:
                holder.ingredientName.setBackgroundResource(R.drawable.warn_red_rectangle);
                holder.ingredientName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                break;
            case 2:
                holder.ingredientName.setBackgroundResource(R.drawable.warn_yellow_rectangle);
                holder.ingredientName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                break;
            case 3:
                holder.ingredientName.setBackgroundResource(R.drawable.warn_green_rectangle);
                holder.ingredientName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
