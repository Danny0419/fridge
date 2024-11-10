package com.example.fragment_test.ScannerList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fragment_test.R;
import com.example.fragment_test.ServerAPI.CombinedIngredient;
import java.util.List;

public class JumpAdapter extends RecyclerView.Adapter<JumpAdapter.ViewHolder> {
    private List<Item> items;
    private List<CombinedIngredient> ingredients;

    // 修正建構子
    public JumpAdapter(List<Item> items, List<CombinedIngredient> ingredients) {
        this.items = items;
        this.ingredients = ingredients;
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

        public void bind(Item item, CombinedIngredient ingredient) {
            // 設定項目編號
            itemId.setText(String.valueOf(getAdapterPosition() + 1));

            if (item != null) {
                // 如果 ingredient 不為 null，顯示資料
                if (ingredient != null) {
                    itemName.setText(ingredient.getIngredient_Name());  // 顯示轉換後的名稱
                    itemSort.setText(ingredient.getIngredients_category());
                    itemExpiration.setText(String.valueOf(ingredient.getExpiration()) + " 天");
                    itemQuantity.setText(item.getQuantity());

                    // 設定該項目為可見
                    itemView.setVisibility(View.VISIBLE);
                } else {
                    // 沒有對應的 ingredient 資料，隱藏該項目
                    itemView.setVisibility(View.GONE);
                }
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
        Item item = items.get(position);
        CombinedIngredient ingredient = null;
        if (ingredients != null && position < ingredients.size()) {
            ingredient = ingredients.get(position);
        }
        holder.bind(item, ingredient);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void updateData(List<Item> newItems, List<CombinedIngredient> newIngredients) {
        this.items = newItems;
        this.ingredients = newIngredients;
        notifyDataSetChanged();
    }
}