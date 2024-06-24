package com.example.fragment_test.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.UpdateData;
import com.example.fragment_test.pojo.Ingredient;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements UpdateData<Ingredient> {
    private ArrayList<Ingredient> ingredient;
    private Context context;

    public MyAdapter(ArrayList<Ingredient> ingredient, Context context) {
        this.ingredient = ingredient;
        this.context = context;
//        this.layoutInflater = layoutInflater;
    }

    @Override
    public void updateData(ArrayList<Ingredient> data) {
        ingredient = data;
        notifyDataSetChanged();
    }


    //    private LayoutInflater layoutInflater;
    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id, name, sort, quantity;
        public CheckBox state;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            id = itemView.findViewById(R.id.id);
//            name = itemView.findViewById(R.id.name);
//            sort = itemView.findViewById(R.id.sort);
//            quantity = itemView.findViewById(R.id.quantity);
//            state = itemView.findViewById(R.id.state);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.e("onBindViewHolder", "正在執行");
        int id = ingredient.get(position).id;
        if (id % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.pink));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        holder.id.setText(Integer.toString(ingredient.get(position).id));
        holder.name.setText(ingredient.get(position).name);
        holder.sort.setText(ingredient.get(position).sort);
        holder.quantity.setText(ingredient.get(position).quantity);
//        Integer state = ingredient.get(position).status;
//        if (1 == state) {
//            holder.state.setChecked(true);
//        }
    }


    @Override
    public int getItemCount() {
        return ingredient.size();
    }
}

