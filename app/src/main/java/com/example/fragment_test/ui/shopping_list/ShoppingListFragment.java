package com.example.fragment_test.ui.shopping_list;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.ShoppingListAdapter;
import com.example.fragment_test.entity.ShoppingIngredient;

import java.util.List;

public class ShoppingListFragment extends Fragment implements View.OnClickListener {

    private ShoppingLIstViewModel mViewModel;
    private RecyclerView shoppingListItemRecycleView;
    private EditText dialogName;
    private EditText dialogSort;
    private EditText dialogQuantity;
    private Dialog dialog;
    private RecyclerView.LayoutManager layoutManager;

    public static ShoppingListFragment newInstance() {
        return new ShoppingListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = initialize(inflater, container);
        mViewModel = new ShoppingLIstViewModel(getActivity().getApplication());

        mViewModel.loadShoppingList().observe(getViewLifecycleOwner(), new Observer<List<ShoppingIngredient>>() {
            @Override
            public void onChanged(List<ShoppingIngredient> shoppingIngredients) {
                shoppingListItemRecycleView.setLayoutManager(layoutManager);
                shoppingListItemRecycleView.setAdapter(new ShoppingListAdapter(shoppingIngredients, getContext()));
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ShoppingLIstViewModel.class);
        // TODO: Use the ViewModel
    }

    private View initialize(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        shoppingListItemRecycleView = view.findViewById(R.id.shoppingListItemRecyclerview);
        layoutManager = new LinearLayoutManager(getContext());

        Button addItemButton = view.findViewById(R.id.addNewShoppingListItemButton);
        addItemButton.setOnClickListener(this);

        View viewDialog = inflater.inflate(R.layout.shoppinglist_alter_dialog, container, false);
        Button dialogCancelBtn = viewDialog.findViewById(R.id.cancel_button);
        Button dialogConfirmBtn = viewDialog.findViewById(R.id.confirm_button);
        dialogName = viewDialog.findViewById(R.id.name);
        dialogSort = viewDialog.findViewById(R.id.sort);
        dialogQuantity = viewDialog.findViewById(R.id.quantity);


        dialog = new Dialog(getContext());
        dialog.setContentView(viewDialog);
        dialog.setCancelable(false);

        dialogCancelBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int clickedId = v.getId();

        if (clickedId == R.id.addNewShoppingListItemButton) {
            dialog.show();
        } else if (clickedId == R.id.cancel_button) {
            emptyEditViewsContent();
            dialog.dismiss();
        } else if (clickedId == R.id.confirm_button) {

        }
    }

    private void emptyEditViewsContent() {
        dialogName.setText("");
        dialogSort.setText("");
        dialogQuantity.setText("");
    }
}