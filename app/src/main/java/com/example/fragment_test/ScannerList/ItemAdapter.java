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
       // holder.textViewName.setText(item.getName()); //原本的商品名稱
       // holder.textViewQuantity.setText("數量: " + item.getQuantity()); //商品數量
        //holder.textViewAmount.setText("金額: " + item.getAmount()); // 金額，不顯示
        //holder.textViewCategory.setText("商品種類: " + item.getChangedName());//商品種類
        //holder.textViewChangename.setText("轉換後商品名稱: " + item.getChangedName());//轉換後名稱
        //holder.textViewExpiration.setText("保存期限: " + item.getExpiration());//保存期限
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
        TextView textViewName, textViewQuantity,textViewCategory, textViewChangename, textViewExpiration;

        ItemViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewChangename = itemView.findViewById(R.id.textViewChangename);
            textViewExpiration = itemView.findViewById(R.id.textViewExpiration);

        }
    }
}