package com.example.fragment_test.adapter;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.entity.RefrigeratorIngredient;

import java.util.List;

public class RefrigeratorEachIngredientAdapter extends RecyclerView.Adapter<RefrigeratorEachIngredientAdapter.RefrigeratorKindViewHolder>{

    private List<RefrigeratorIngredient> kindOfIngredient;
    private Dialog ingredientDetail;
    private RefrigeratorAdapter.OnClickListener onClickListener;

    public RefrigeratorEachIngredientAdapter(List<RefrigeratorIngredient> kindOfIngredient, Dialog ingredientDetail) {
        this.kindOfIngredient = kindOfIngredient;
        this.ingredientDetail = ingredientDetail;
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
                .inflate(R.layout.refrigerator_card_item, parent, false);
        return new RefrigeratorKindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RefrigeratorKindViewHolder holder, int position) {
        RefrigeratorIngredient ingredient = kindOfIngredient.get(position);
        holder.ingredientImg.setText(ingredient.img);
        holder.ingredientName.setText(ingredient.name);
        holder.ingredientExpr.setText("保存期限" + ingredient.expiration);
        holder.ingredientQuan.setText(Integer.toString(ingredient.quantity));
        holder.itemView.setOnClickListener(view -> onClickListener.onClick(position, ingredient));
    }

    public void setOnClickListener(RefrigeratorAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return kindOfIngredient.size();
    }
}
