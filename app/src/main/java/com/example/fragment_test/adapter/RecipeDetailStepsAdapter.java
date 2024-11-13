package com.example.fragment_test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.entity.Step;

import java.util.List;

public class RecipeDetailStepsAdapter extends RecyclerView.Adapter<RecipeDetailStepsAdapter.RecipeDetailStepsViewHolder> {
    List<Step> recipeSteps;

    public class RecipeDetailStepsViewHolder extends RecyclerView.ViewHolder {
        TextView stepOrder;
        ImageView stepPic;
        TextView stepContent;

        public RecipeDetailStepsViewHolder(@NonNull View itemView) {
            super(itemView);
            stepOrder = itemView.findViewById(R.id.step);
            stepContent = itemView.findViewById(R.id.step_describe);
        }
    }

    public RecipeDetailStepsAdapter(List<Step> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    @NonNull
    @Override
    public RecipeDetailStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeDetailStepsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_steps_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailStepsViewHolder holder, int position) {
        Step step = recipeSteps.get(position);
        holder.stepOrder.setText("步驟" + step.stepNum);
        holder.stepContent.setText(step.stepDetail);
    }

    @Override
    public int getItemCount() {
        return recipeSteps.size();
    }
}
