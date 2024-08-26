package com.example.fragment_test.ui.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.RecipeAdapter;
import com.example.fragment_test.databinding.FragmentRecipeBinding;
import com.example.fragment_test.entity.Recipe;

import java.util.List;

public class RecipeFragment extends Fragment {

    private FragmentRecipeBinding binding;
    private RecipeViewModel mViewModel;
    private RecyclerView recipeRecyclerView;
    private boolean isCollectionSelected = false;

    public static RecipeFragment newInstance() {
        return new RecipeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(RecipeViewModel.class);
        mViewModel.loadRecommendRecipes();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentRecipeBinding.inflate(inflater);
        recipeRecyclerView = binding.recipeRecyclerView;

        FragmentActivity fragmentActivity = requireActivity();
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main2);
        fragmentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.recipe_toolbar, menu);
            }

            //toolbar點擊
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main2);
                //規劃
                if (itemId == R.id.schedule) {
                    //在跳轉之前先將原頁面退回到首頁，防止navbar一直導到跳轉頁面
                    navController.popBackStack(R.id.navigation_home, false);
                    //頁面跳轉
                    navController.navigate(R.id.navigation_schedule);
                }
                //收藏
                else if (itemId == R.id.collection) {
                    mViewModel.showCollectionRecipe();
                    //如果已經點選過，變回為填滿
                    if (isCollectionSelected) {
                        menuItem.setIcon(R.drawable.bookmark);
                    }
                    //若未被點選過，變成填滿
                    else {
                        menuItem.setIcon(R.drawable.bookmark__filled);
                    }
                    isCollectionSelected = !isCollectionSelected; // 更新狀態
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.STARTED);

        mViewModel.getRecipes()
                .observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
                    @Override
                    public void onChanged(List<Recipe> recipes) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recipeRecyclerView.setLayoutManager(linearLayoutManager);
                        RecipeAdapter recipeAdapter = new RecipeAdapter(getContext(), recipes);
                        recipeAdapter.setListener((position, recipe) -> {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("recipe", recipe);
                            navController.navigate(R.id.navigation_recipe_detail, bundle);

                        });
                        recipeRecyclerView.setAdapter(recipeAdapter);
                    }
                });
        return binding.getRoot();
    }
}