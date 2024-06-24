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
import com.example.fragment_test.pojo.RefrigeratorIngredient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RefrigeratorKindAdapter extends RecyclerView.Adapter<RefrigeratorKindAdapter.RefrigeratorKindViewHolder>{

    private List<RefrigeratorIngredient> kindOfIngredient;


    public RefrigeratorKindAdapter(List<RefrigeratorIngredient> kindOfIngredient) {
        this.kindOfIngredient = kindOfIngredient;
    }

    class RefrigeratorKindViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientImg;
        TextView ingredientName;
        TextView ingredientExpr;
        TextView ingredientQuan;


        public RefrigeratorKindViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImg = itemView.findViewById(R.id.ingredientImg);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            ingredientExpr = itemView.findViewById(R.id.ingredientExpiration);
            ingredientQuan = itemView.findViewById(R.id.ingredientQuan);
        }
    }

    @NonNull
    @Override
    public RefrigeratorKindViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.refrigerator_item, parent, false);
        return new RefrigeratorKindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RefrigeratorKindViewHolder holder, int position) {
        Ingredient ingredient = kindOfIngredient.get(position);
        holder.ingredientImg.setText(ingredient.img);
        holder.ingredientName.setText(ingredient.name);
        holder.ingredientExpr.setText("保存期限");
        holder.ingredientQuan.setText(ingredient.quantity.toString());
    }

    @Override
    public int getItemCount() {
        return kindOfIngredient.size();
    }
}
