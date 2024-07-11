package com.example.fragment_test.ui.shopping_list;

import android.app.Dialog;
import android.os.Bundle;

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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragmentOld#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragmentOld extends Fragment implements View.OnClickListener {

    private ArrayList<ShoppingIngredient> shoppingList;
    private RecyclerView shoppingListItemRecyclerview;
    private Dialog dialog;
    EditText dialogName, dialogSort, dialogQuantity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShoppingListFragmentOld() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoopingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingListFragmentOld newInstance(String param1, String param2) {
        ShoppingListFragmentOld fragment = new ShoppingListFragmentOld();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ;
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        shoppingListItemRecyclerview = view.findViewById(R.id.shoppingListItemRecyclerview);
        View viewDialog = inflater.inflate(R.layout.shoppinglist_alter_dialog, container, false);
        View testButton = view.findViewById(R.id.addNewShoppingListItemButton);
        Button dialogCancelBtn = viewDialog.findViewById(R.id.cancel_button);
        Button dialogConfirmBtn = viewDialog.findViewById(R.id.confirm_button);
        dialogName = viewDialog.findViewById(R.id.name);
        dialogSort = viewDialog.findViewById(R.id.sort);
        dialogQuantity = viewDialog.findViewById(R.id.quantity);


        dialog = new Dialog(getContext());
        dialog.setContentView(viewDialog);
        dialog.setCancelable(false);

        testButton.setOnClickListener(this);
        dialogCancelBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        shoppingListItemRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        shoppingListItemRecyclerview.setAdapter(new ShoppingListAdapter(shoppingList, getContext()));
    }

    public void setShoppingList(ArrayList<ShoppingIngredient> shoppingList) {
        this.shoppingList = shoppingList;
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