package com.example.fragment_test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.entity.RefrigeratorIngredient;

import java.util.List;

public class RefrigeratorIngredientDetailAdapter extends RecyclerView.Adapter<RefrigeratorIngredientDetailAdapter.RefrigeratorIngredientDetailViewHolder> {

    List<RefrigeratorIngredient> ingredientDetailVOList;

    class RefrigeratorIngredientDetailViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientQuantity;
        TextView ingredientPurchaseDate;
        TextView ingredientExpiration;
        TextView daysRemaining;

        public RefrigeratorIngredientDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
            ingredientPurchaseDate = itemView.findViewById(R.id.ingredient_purchase_date);
            ingredientExpiration = itemView.findViewById(R.id.ingredient_expiration);
            daysRemaining = itemView.findViewById(R.id.days_remaining);
        }
    }
    @NonNull
    @Override
    public RefrigeratorIngredientDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.refrigerator_ingredient_detail, parent, false);
        return new RefrigeratorIngredientDetailViewHolder(view);
    }

    public RefrigeratorIngredientDetailAdapter(List<RefrigeratorIngredient> ingredientDetailVOList) {
        this.ingredientDetailVOList = ingredientDetailVOList;
    }

    @Override
    public void onBindViewHolder(@NonNull RefrigeratorIngredientDetailViewHolder holder, int position) {
        RefrigeratorIngredient ingredient = ingredientDetailVOList.get(position);
        holder.ingredientQuantity.setText("" + ingredient.quantity);
        holder.ingredientPurchaseDate.setText("" + ingredient.purchaseDate);
        holder.ingredientExpiration.setText("" + ingredient.expiration);
        holder.daysRemaining.setText("" + ingredient.daysRemaining);
    }

    @Override
    public int getItemCount() {
        return ingredientDetailVOList.size();
    }
}
