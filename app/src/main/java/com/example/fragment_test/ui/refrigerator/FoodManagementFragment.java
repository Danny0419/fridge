package com.example.fragment_test.ui.refrigerator;

import static com.example.fragment_test.utils.setListBackground.setListBackgroundColor;
import static com.example.fragment_test.utils.setListBackground.setListUnderLine;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.RefrigeratorAdapter;
import com.example.fragment_test.adapter.RefrigeratorIngredientDetailAdapter;
import com.example.fragment_test.databinding.FragmentRefrigeratorBinding;
import com.example.fragment_test.databinding.RefrigeratorItemDetailDialogBinding;
import com.example.fragment_test.utils.DividerItemDecoration;
import com.google.android.material.tabs.TabLayout;

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

    private FragmentRefrigeratorBinding refrigeratorBinding;
    private RefrigeratorItemDetailDialogBinding refrigeratorItemDetailDialogBinding;
    TabLayout tabLayout;
    LinearLayoutManager layoutManager;
    RecyclerView ingredientContainer;
    private Dialog ingredientDetail;
    private FoodManagementViewModel viewModel;

    public FoodManagementFragment() {
        // Required empty public constructor
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
        this.viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(FoodManagementViewModel.class);
        this.viewModel.loadRefrigeratorIngredients();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = initialize(inflater, container);

        viewModel.getRefrigeratorIngredients().observe(getViewLifecycleOwner(), (refrigeratorMap) ->{
                layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                ingredientContainer.setLayoutManager(layoutManager);
                RefrigeratorAdapter adapter = new RefrigeratorAdapter(getContext(), refrigeratorMap);
                adapter.setOnClickListener((position, refrigeratorIngredient) -> {
                    refrigeratorItemDetailDialogBinding.name.setText(refrigeratorIngredient.name);
                    viewModel.seeIngredientDetail(refrigeratorIngredient);
                    viewModel.getIngredientDetails()
                            .observe(getViewLifecycleOwner(), (ingredientVOS) -> {
                                RecyclerView refrigeratorItemDetail = refrigeratorItemDetailDialogBinding.refrigeratorItemDetail;
                                refrigeratorItemDetail.setLayoutManager(new LinearLayoutManager(getContext()));
                                RefrigeratorIngredientDetailAdapter refrigeratorIngredientDetailAdapter = new RefrigeratorIngredientDetailAdapter(ingredientVOS);
                                refrigeratorIngredientDetailAdapter.setTextChangedListener((o, q) -> {
                                    o.setQuantity(q);
                                    viewModel.editIngredientQuantity(o);
                                });
                                refrigeratorItemDetail.setAdapter(refrigeratorIngredientDetailAdapter);
                                ingredientDetail.show();
                            });
                });
                ingredientContainer.setAdapter(adapter);
                ingredientContainer.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) ->{
                        tabLayout.setScrollPosition(layoutManager.findFirstVisibleItemPosition(), 0, true);
                });

                //設定列表加下底線(最後一行除外)
//            ingredientContainer.addItemDecoration(new DividerItemDecoration(getContext()));

        });



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

        addToolbar();

        return view;
    }

    private View initialize(LayoutInflater inflater, ViewGroup container){
        refrigeratorBinding = FragmentRefrigeratorBinding.inflate(inflater, container, false);
        ingredientContainer = refrigeratorBinding.kindsOfIngredientContainer;

        refrigeratorItemDetailDialogBinding = RefrigeratorItemDetailDialogBinding.inflate(inflater, container, false);
        ingredientDetail = new Dialog(getContext());
        ingredientDetail.setContentView(refrigeratorItemDetailDialogBinding.getRoot());

        // 應急用調整彈跳視窗大小
        WindowManager.LayoutParams layoutParams = ingredientDetail.getWindow().getAttributes();
        layoutParams.width = 1000;
        layoutParams.height = 1250;
        ingredientDetail.getWindow().setAttributes(layoutParams);

        initTab(refrigeratorBinding.getRoot());
        return refrigeratorBinding.getRoot();
    }


    private void initTab(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        String[] stringArray = getActivity().getResources().getStringArray(R.array.kinds_of_ingredient);
        for (int i = 0, n = stringArray.length; i < n; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(stringArray[i]));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ingredientContainer.setLayoutManager(layoutManager);
    }

    //toolbar
    private void addToolbar() {
        FragmentActivity fragmentActivity = requireActivity();
        fragmentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.schedule_toolbar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
//                int itemId = menuItem.getItemId();
//                NavController navController = Navigation.findNavController(getActivity(),
//                        R.id.nav_host_fragment_activity_main2);
//                if (itemId == R.id.scan) {
//                    navController.navigate(R.id.action_navigation_home_to_navigation_camera);
//                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.STARTED);
    }
}