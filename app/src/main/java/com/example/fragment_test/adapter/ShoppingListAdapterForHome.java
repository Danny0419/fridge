package com.example.fragment_test.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.vo.ShoppingItemVO;

import java.util.List;

public class ShoppingListAdapterForHome extends RecyclerView.Adapter <ShoppingListAdapterForHome.ShoppingItemViewHolderForHome>{

    private List<ShoppingItemVO> shoppingItems;

    public class ShoppingItemViewHolderForHome extends RecyclerView.ViewHolder{

        TextView ingredientName;
        TextView ingredientQuantity;

        public ShoppingItemViewHolderForHome(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
            ingredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
        }
    }

    public ShoppingListAdapterForHome(List<ShoppingItemVO> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }

    @NonNull
    @Override
    public ShoppingItemViewHolderForHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_shopping_list_container, parent, false);
        return new ShoppingItemViewHolderForHome(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ShoppingItemViewHolderForHome holder, int position) {
        ShoppingItemVO shoppingItemVO = shoppingItems.get(position);
        holder.ingredientName.setText(shoppingItemVO.name);
        holder.ingredientQuantity.setText(shoppingItemVO.sumOfQuantity + "");
    }


    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }
}
