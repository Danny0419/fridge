package com.example.fragment_test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.vo.ShoppingItemVO;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {

    List<ShoppingItemVO> shoppingItems;
    ShoppingItemEditedListener shoppingItemEditedListener;

    public interface ShoppingItemEditedListener {
        void shoppingItemEdited(ShoppingItemVO shoppingItemVO);
    }

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

    public ShoppingListAdapter(List<ShoppingItemVO> shoppingItems) {
        this.shoppingItems = shoppingItems;
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
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.pink));
        }

        ShoppingItemVO shoppingItemVO = shoppingItems.get(position);
        holder.position.setText(Integer.toString(position+1));
        holder.shoppingItemName.setText(shoppingItemVO.name);
        holder.shoppingItemSort.setText(shoppingItemVO.sort);
        holder.shoppingItemQuantity.setText(shoppingItemVO.sumOfQuantity + " g");

        holder.itemView
                .setOnClickListener(view -> this.shoppingItemEditedListener.shoppingItemEdited(shoppingItemVO));
    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    public void setShoppingItemEditedListener(ShoppingItemEditedListener shoppingItemEditedListener) {
        this.shoppingItemEditedListener = shoppingItemEditedListener;
    }
}
