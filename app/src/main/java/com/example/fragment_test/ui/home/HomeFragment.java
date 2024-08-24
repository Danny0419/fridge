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
import com.example.fragment_test.ui.refrigerator.FoodManagementViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                    LocalDate now = LocalDate.now();
                    String date = DateTimeFormatter.BASIC_ISO_DATE.format(now);
                    LocalDate threeDaysLater = now.plusDays(3);
                    String threeDaysLaterStr = DateTimeFormatter.BASIC_ISO_DATE.format(threeDaysLater);
                    List<RefrigeratorIngredient> ingredients = List.of(
                            new RefrigeratorIngredient(0, "牛排", 3, "牛排照片", "肉類", Integer.parseInt(date), Integer.parseInt(threeDaysLaterStr)),
                            new RefrigeratorIngredient(0, "牛排", 3, "牛排照片", "肉類", Integer.parseInt(date), Integer.parseInt(threeDaysLaterStr)),
                            new RefrigeratorIngredient(0, "牛小排", 4, "牛小排照片", "肉類", Integer.parseInt(date), Integer.parseInt(threeDaysLaterStr))
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