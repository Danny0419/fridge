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

public class SchedulePreparedRecipeAdapter extends RecyclerView.Adapter<SchedulePreparedRecipeAdapter.SchedulePreparedRecipeViewHolder> {
    List<Recipe> preparedRecipes;

    class SchedulePreparedRecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView foodPicture;
        TextView foodName;

        public SchedulePreparedRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            foodPicture = itemView.findViewById(R.id.food_picture);
            foodName = itemView.findViewById(R.id.food_name);
        }
    }

    public SchedulePreparedRecipeAdapter(List<Recipe> preparedRecipes) {
        this.preparedRecipes = preparedRecipes;
    }

    @NonNull
    @Override
    public SchedulePreparedRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new SchedulePreparedRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchedulePreparedRecipeViewHolder holder, int position) {
        Recipe preparedRecipe = preparedRecipes.get(position);
        holder.foodName.setText(preparedRecipe.name);
    }

    @Override
    public int getItemCount() {
        return preparedRecipes.size();
    }
}
