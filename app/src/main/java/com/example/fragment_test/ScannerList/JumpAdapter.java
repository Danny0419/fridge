package com.example.fragment_test.ScannerList;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fragment_test.R;
import com.example.fragment_test.ServerAPI.CombinedIngredient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JumpAdapter extends RecyclerView.Adapter<JumpAdapter.ViewHolder> {

    private List<Item> items;
    private List<CombinedIngredient> ingredients;


    public JumpAdapter(List<Item> items, List<CombinedIngredient> ingredients) {
        this.items = items != null ? items : new ArrayList<>();
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemId, itemName, itemSort, itemQuantity, itemExpiration;

        public ViewHolder(View view) {
            super(view);
            itemId = view.findViewById(R.id.itemId);
            itemName = view.findViewById(R.id.itemName);
            itemSort = view.findViewById(R.id.itemSort);
            itemQuantity = view.findViewById(R.id.itemQuantity);
            itemExpiration = view.findViewById(R.id.itemExpiration);
        }

        public void bind(Item item, CombinedIngredient ingredient, int position) {
            itemId.setText(String.valueOf(position + 1));



            if (item != null && ingredient != null) {
                itemName.setText(ingredient.getIngredient_Name());
                itemSort.setText(ingredient.getIngredients_category());
                itemExpiration.setText(String.valueOf(ingredient.getExpiration()) + " 天");
                itemQuantity.setText(ingredient.getGrams());
                Log.i("TAG","send UI: " + ingredient.getIngredient_Name());
            } else {
               
                itemName.setText("");
                itemSort.setText("");
                itemExpiration.setText("");
                itemQuantity.setText("");
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scan_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = null;
        CombinedIngredient ingredient = null;

        // 確保 position 不超過列表大小
        if (position < items.size()) {
            item = items.get(position);
        }
        if (position < ingredients.size()) {
            ingredient = ingredients.get(position);
        }

        holder.bind(item, ingredient, position);
    }

    @Override
    public int getItemCount() {
        // 返回兩個列表中較長的一個
        return Math.max(items.size(), ingredients.size());
    }

    public void updateData(List<Item> newItems, List<CombinedIngredient> newIngredients) {
        this.items = newItems != null ? newItems : new ArrayList<>();
        this.ingredients = newIngredients != null ? newIngredients : new ArrayList<>();
        notifyDataSetChanged();
        Log.d("JumpAdapter", "Data updated: items=" + items.size() + ", ingredients=" + ingredients.size());
    }

    public void swapItems(int fromPosition, int toPosition) {
        // 檢查索引是否有效
        if (fromPosition < 0 || toPosition < 0 ||
                fromPosition >= items.size() || toPosition >= items.size() ||
                fromPosition >= ingredients.size() || toPosition >= ingredients.size()) {
            return;
        }

        // 檢查 items 和 ingredients 列表的大小是否一致
        if (items.size() != ingredients.size()) {
            Log.e("JumpAdapter", "Item and ingredient lists do not match in size.");
            return;  // 如果大小不一致，則不執行交換
        }

        // 進行交換
        Collections.swap(items, fromPosition, toPosition);
        Collections.swap(ingredients, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }
}
