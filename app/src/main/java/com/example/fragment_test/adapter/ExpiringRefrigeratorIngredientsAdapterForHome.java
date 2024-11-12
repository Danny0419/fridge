package com.example.fragment_test.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.vo.RefrigeratorIngredientDetailVO;

import java.util.List;

public class ExpiringRefrigeratorIngredientsAdapterForHome extends RecyclerView.Adapter<ExpiringRefrigeratorIngredientsAdapterForHome.ExpiringRefrigeratorIngredientsViewHolderForHome> {

    private List<RefrigeratorIngredientDetailVO> refrigeratorIngredients;

    public class ExpiringRefrigeratorIngredientsViewHolderForHome extends RecyclerView.ViewHolder {

        TextView ingredientName;
        TextView ingredientQuantity;
        TextView remindNumber;

        public ExpiringRefrigeratorIngredientsViewHolderForHome(@NonNull View itemView) {
            super(itemView);
            this.ingredientName = itemView.findViewById(R.id.ingredient_name);
            this.ingredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
            this.remindNumber = itemView.findViewById(R.id.remind_number);
        }
    }

    public ExpiringRefrigeratorIngredientsAdapterForHome(List<RefrigeratorIngredientDetailVO> refrigeratorIngredients) {
        this.refrigeratorIngredients = refrigeratorIngredients;
    }

    @NonNull
    @Override
    public ExpiringRefrigeratorIngredientsViewHolderForHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.home_ingredients_remind_item, parent, false);
        return new ExpiringRefrigeratorIngredientsViewHolderForHome(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExpiringRefrigeratorIngredientsViewHolderForHome holder, int position) {
        RefrigeratorIngredientDetailVO refrigeratorIngredient = refrigeratorIngredients.get(position);
        holder.ingredientName.setText(refrigeratorIngredient.name);
        holder.ingredientQuantity.setText(refrigeratorIngredient.quantity + " g");
        holder.remindNumber.setText(refrigeratorIngredient.daysRemaining + "");
    }

    @Override
    public int getItemCount() {
        return refrigeratorIngredients.size();
    }
}
