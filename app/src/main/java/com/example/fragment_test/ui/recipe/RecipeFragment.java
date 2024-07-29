package com.example.fragment_test.ui.recipe;

import androidx.core.view.MenuProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.RecipeAdapter;
import com.example.fragment_test.databinding.FragmentRecipeBinding;
import com.example.fragment_test.entity.Ingredient;
import com.example.fragment_test.entity.Recipe;

import java.util.ArrayList;

public class RecipeFragment extends Fragment {

    private FragmentRecipeBinding binding;
    private RecipeViewModel mViewModel;
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private boolean iscollectionSelected=false;

    public static RecipeFragment newInstance() {
        return new RecipeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        binding = FragmentRecipeBinding.inflate(inflater);

        FragmentActivity fragmentActivity = requireActivity();
        fragmentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.recipe_toolbar, menu);
            }

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
                    //如果已經點選過，變回為填滿
                    if (iscollectionSelected){
                        menuItem.setIcon(R.drawable.bookmark);
                    }
                    //若未被點選過，變成填滿
                    else {
                        menuItem.setIcon(R.drawable.bookmark__filled);
                    }
                    iscollectionSelected = !iscollectionSelected; // 更新狀態
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.STARTED);

        return binding.getRoot();
    }
}