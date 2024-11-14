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

public class MealsPrepareScheduleEachRecipeAdapter extends RecyclerView.Adapter<MealsPrepareScheduleEachRecipeAdapter.ScheduleEachRecipeViewHolder> {
    private List<Recipe> recipes;
    private OnClickListener onClickListener;
    private int date;

    class ScheduleEachRecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeTitle;
        ImageView recipeImg;

        public ScheduleEachRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.recipeTitle = itemView.findViewById(R.id.recipe_title);
            this.recipeImg = itemView.findViewById(R.id.recipe_img);
        }
    }

    public interface OnClickListener {
        void onClick(int date, Recipe recipe);
    }

    public MealsPrepareScheduleEachRecipeAdapter(List<Recipe> recipes, int date) {
        this.recipes = recipes;
        this.date = date;
    }

    @NonNull
    @Override
    public ScheduleEachRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_recipe_card_item, parent, false);
        return new ScheduleEachRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleEachRecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.recipeImg.setImageBitmap(recipe.pic);
        holder.recipeTitle.setText(recipe.name);
        holder.itemView.setOnClickListener((v) -> onClickListener.onClick(date, recipe));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
