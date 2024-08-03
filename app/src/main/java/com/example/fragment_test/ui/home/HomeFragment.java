package com.example.fragment_test.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.fragment_test.R;
import com.example.fragment_test.databinding.FragmentHomeBinding;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.repository.RefrigeratorIngredientRepository;
import com.example.fragment_test.ui.refrigerator.FoodManagementViewModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        addToolbar();

        return root;
    }

    private void addToolbar() {
        FragmentActivity fragmentActivity = requireActivity();
        fragmentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.home_toolbar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                NavController navController = Navigation.findNavController(getActivity(),
                        R.id.nav_host_fragment_activity_main2);
                if (itemId == R.id.scan) {
                    navController.navigate(R.id.action_navigation_home_to_navigation_camera);
                } else if (itemId == R.id.test) {
                    FoodManagementViewModel foodManagementViewModel = new FoodManagementViewModel(getActivity().getApplication());
                    List<RefrigeratorIngredient> ingredients = List.of(
                            new RefrigeratorIngredient("牛排", 3, "肉類",
                                    "牛排照片", 5, "2024-07-31", 0),
                            new RefrigeratorIngredient("牛排", 3, "肉類",
                                    "牛排照片", 5, "2024-07-31", 0),
                            new RefrigeratorIngredient("牛小排", 3, "肉類",
                                    "牛排照片", 5, "2024-07-31", 0),
                            new RefrigeratorIngredient("牛小排", 3, "肉類",
                                    "牛排照片", 5, "2024-07-31", 0),
                            new RefrigeratorIngredient("牛排", 3, "肉類",
                                    "牛排照片", 5, "2024-07-31", 0)
                    );
                    foodManagementViewModel.addRefrigeratorIngredients(ingredients);
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.STARTED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}