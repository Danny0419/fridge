package com.example.fragment_test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.pojo.Ingredient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RefrigeratorKindAdapter extends RecyclerView.Adapter{

    private List<Ingredient> kindOfIngredient;


    public RefrigeratorKindAdapter(List<Ingredient> kindOfIngredient) {
        this.kindOfIngredient = kindOfIngredient;
    }

    class RefrigeratorKindViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientImg;
        TextView ingredientName;


        public RefrigeratorKindViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImg = itemView.findViewById(R.id.ingredientImg);
            ingredientName = itemView.findViewById(R.id.ingredientName);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new RefrigeratorKindAdapter.RefrigeratorKindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Ingredient ingredient = kindOfIngredient.get(position);
        ((RefrigeratorKindViewHolder)holder).ingredientImg.setText(ingredient.getImg());
        ((RefrigeratorKindViewHolder)holder).ingredientName.setText(ingredient.getName());
    }

    @Override
    public int getItemCount() {
        return kindOfIngredient.size();
    }
}
