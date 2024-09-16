package com.example.fragment_test.ScannerList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items;

    public ItemAdapter(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receipt, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.textViewName.setText(item.getName());
        holder.textViewQuantity.setText("數量: " + item.getQuantity());
        //holder.textViewAmount.setText("金額: " + item.getAmount()); //金額不顯示
        holder.textViewChangename.setText("轉換後商品名稱: " );
        holder.textViewExpiration.setText("保存期限: " );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<Item> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewQuantity, textViewChangename, textViewExpiration;

        ItemViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewChangename = itemView.findViewById(R.id.textViewChangename);
            textViewExpiration = itemView.findViewById(R.id.textViewExpiration);
        }
    }
}