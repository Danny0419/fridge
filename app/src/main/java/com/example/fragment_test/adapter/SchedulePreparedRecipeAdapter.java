package com.example.fragment_test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.entity.RecipeWithPreRecipeId;

import java.util.List;

public class SchedulePreparedRecipeAdapter extends RecyclerView.Adapter<SchedulePreparedRecipeAdapter.SchedulePreparedRecipeViewHolder> {
    List<RecipeWithPreRecipeId> preparedRecipes;
    OnClickListener onclickListener;

    class SchedulePreparedRecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipePicture;
        TextView recipeName;

        public SchedulePreparedRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipePicture = itemView.findViewById(R.id.recipe_picture);
            recipeName = itemView.findViewById(R.id.recipe_name);
        }
    }

    public interface OnClickListener {
        void onclick(RecipeWithPreRecipeId recipe);
    }

    public SchedulePreparedRecipeAdapter(List<RecipeWithPreRecipeId> preparedRecipes) {
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
        RecipeWithPreRecipeId preparedRecipe = preparedRecipes.get(position);
        holder.recipeName.setText(preparedRecipe.recipe.name);
        holder.itemView.setOnClickListener(view -> onclickListener.onclick(preparedRecipe));
    }

    @Override
    public int getItemCount() {
        return preparedRecipes.size();
    }

    public void setOnclickListener(OnClickListener onclickListener) {
        this.onclickListener = onclickListener;
    }
}
