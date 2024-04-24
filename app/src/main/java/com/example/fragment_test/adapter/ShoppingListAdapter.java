package com.example.fragment_test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.pojo.Ingredient;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.MyViewHolder> {

     ArrayList<Ingredient> shoppingItems;
     Context context;


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shoppingItemId, shoppingItemName, shoppingItemSort, shoppingItemQuantity;
        CheckBox shoppingItemState;
        MyViewHolder(View view) {
            super(view);
            shoppingItemId = view.findViewById(R.id.shoppingItemId);
            shoppingItemName = view.findViewById(R.id.shoppingItemName);
            shoppingItemSort = view.findViewById(R.id.shoppingItemSort);
            shoppingItemQuantity = view.findViewById(R.id.shoppingItemQuantity);
            shoppingItemState = view.findViewById(R.id.shoppingItemState);
        }
    }

    public ShoppingListAdapter(ArrayList<Ingredient> shoppingItems, Context context) {
        this.shoppingItems = shoppingItems;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int id = shoppingItems.get(position).getId();
        if (id % 2 != 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.pink));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        holder.shoppingItemId.setText(shoppingItems.get(position).getId().toString());
        holder.shoppingItemName.setText(shoppingItems.get(position).getName().toString());
        holder.shoppingItemSort.setText(shoppingItems.get(position).getSort().toString());
        holder.shoppingItemQuantity.setText(shoppingItems.get(position).getQuantity().toString());
        Integer state = shoppingItems.get(position).getState();
        if (1 == state) {
            holder.shoppingItemState.setChecked(true);
        }
    }


    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }
}
