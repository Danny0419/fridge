package com.example.fragment_test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.vo.ShoppingItemVO;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {

    List<ShoppingItemVO> shoppingItems;
    Context context;

    class ShoppingListViewHolder extends RecyclerView.ViewHolder {

        TextView position, shoppingItemName, shoppingItemSort, shoppingItemQuantity;
        CheckBox shoppingItemState;
        ImageButton editBtn, deleteBtn;

        ShoppingListViewHolder(View view) {
            super(view);
            position = view.findViewById(R.id.position);
            shoppingItemName = view.findViewById(R.id.shoppingItemName);
            shoppingItemSort = view.findViewById(R.id.shoppingItemSort);
            shoppingItemQuantity = view.findViewById(R.id.shoppingItemQuantity);
            shoppingItemState = view.findViewById(R.id.shoppingItemState);
            editBtn = view.findViewById(R.id.btn_edit);
            deleteBtn = view.findViewById(R.id.btn_delete);
        }
    }

    public ShoppingListAdapter(List<ShoppingItemVO> shoppingItems, Context context) {
        this.shoppingItems = shoppingItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_item, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        if (position % 2 != 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.pink));
        }

        holder.position.setText(Integer.toString(position));
        holder.shoppingItemName.setText(shoppingItems.get(position).name);
        holder.shoppingItemSort.setText(shoppingItems.get(position).sort);
        holder.shoppingItemQuantity.setText(Integer.toString(shoppingItems.get(position).sumOfQuantity));
//        Integer state = shoppingItems.get(position).getState();
//        if (1 == state) {
//            holder.shoppingItemState.setChecked(true);
//        }

        holder.editBtn.setOnClickListener(view -> showEditDialog(position));

        holder.deleteBtn.setOnClickListener(view -> showDeleteDialog(position));
    }

    private void showEditDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("编辑项")
                .setMessage("编辑此购物项？")
                .setPositiveButton("确认", (dialog, which) -> {
                    // 编辑逻辑
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("确认删除")
                .setMessage("确定要删除此购物项吗？")
                .setPositiveButton("删除", (dialog, which) -> {
                    // 删除逻辑
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }
}
