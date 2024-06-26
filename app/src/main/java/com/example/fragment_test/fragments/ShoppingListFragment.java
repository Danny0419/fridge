package com.example.fragment_test.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.ShoppingListAdapter;
import com.example.fragment_test.pojo.ShoppingIngredient;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment {

    private ArrayList<ShoppingIngredient> shoppingList;
    private RecyclerView shoppingListItemContainer;
    private Dialog dialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShoppingListFragment() {
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
    public static ShoppingListFragment newInstance(String param1, String param2) {
        ShoppingListFragment fragment = new ShoppingListFragment();
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
        shoppingListItemContainer = view.findViewById(R.id.shoppingListItemContainer);
        View testButton = view.findViewById(R.id.testButton);

        dialog = new Dialog(getContext());
        View viewDialog = inflater.inflate(R.layout.shoppinglist_alter_dialog, container, false);
        dialog.setContentView(viewDialog);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        shoppingListItemContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        shoppingListItemContainer.setAdapter(new ShoppingListAdapter(shoppingList,getContext()));
    }

    public void setShoppingList(ArrayList<ShoppingIngredient> shoppingList) {
        this.shoppingList = shoppingList;
    }
}