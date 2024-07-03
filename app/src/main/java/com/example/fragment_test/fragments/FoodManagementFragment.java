package com.example.fragment_test.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.RefrigeratorAdapter;
import com.example.fragment_test.pojo.RefrigeratorIngredient;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodManagementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodManagementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TabLayout tabLayout;
    LinearLayoutManager layoutManager;
    RecyclerView ingredientContainer;
    private Map<String, ArrayList<RefrigeratorIngredient>> refrigeratorIngredients;
    private Dialog ingredientDetail;

    public FoodManagementFragment() {
        // Required empty public constructor
    }

    public FoodManagementFragment(Map<String, ArrayList<RefrigeratorIngredient>> refrigeratorIngredients) {
        this.refrigeratorIngredients = refrigeratorIngredients;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodManagementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodManagementFragment newInstance(String param1, String param2) {
        FoodManagementFragment fragment = new FoodManagementFragment();
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
        View view = inflater.inflate(R.layout.fragment_rerigerator, container, false);
        View dialogView = inflater.inflate(R.layout.refrigerator_item_detail_dialog, container, false);
        ingredientDetail = new Dialog(getContext());
        ingredientDetail.setContentView(dialogView);
        initValuable(view);
        initTab();
        LinearSmoothScroller scroller = new LinearSmoothScroller(getContext()) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                scroller.setTargetPosition(tab.getPosition());
                layoutManager.startSmoothScroll(scroller);
                layoutManager.startSmoothScroll(scroller);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ingredientContainer.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
        return view;
    }
    private void initValuable(View view){
        ingredientContainer = view.findViewById(R.id.kinds_of_ingredient_container);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }
    private void initTab() {
        String[] stringArray = getActivity().getResources().getStringArray(R.array.foodManagementTabLayoutTag);
        for (int i = 0, n = stringArray.length; i < n; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(stringArray[i]));
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        setRecyclerView();
        ingredientContainer.setAdapter(new RefrigeratorAdapter(getContext(), refrigeratorIngredients));
        ingredientContainer.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                tabLayout.setScrollPosition(layoutManager.findFirstVisibleItemPosition(), 0, true);
            }
        });
    }
    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ingredientContainer.setLayoutManager(layoutManager);
    }
}