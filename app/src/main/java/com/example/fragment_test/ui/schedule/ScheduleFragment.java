package com.example.fragment_test.ui.schedule;

import static com.example.fragment_test.utils.setListBackground.setListBackgroundColor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.ScheduleAdapter;
import com.example.fragment_test.databinding.FragmentScheduleBinding;
import com.example.fragment_test.ui.recipe.RecipeViewModel;

import java.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {
    private FragmentScheduleBinding scheduleBinding;
    private ScheduleViewModel scheduleViewModel;
    private RecipeViewModel recipeViewModel;
    private final LocalDate[] aWeek = new LocalDate[7];
    private ListView scheduleContainer;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()));
        scheduleViewModel = viewModelProvider.get(ScheduleViewModel.class);
        recipeViewModel = viewModelProvider.get(RecipeViewModel.class);

        setAWeek();
    }

    private void setAWeek() {
        LocalDate today = LocalDate.now();
//        aWeek[today.getDayOfWeek().getValue() - 1] = today;
        int todayIndex = today.getDayOfWeek().getValue() - 1;
        for (int i = 0; i < 7; i++) {
            aWeek[i] = today.plusDays(i);
        }
//        for (int i = 1; i < 7; i++) {
//            LocalDate date = today.plusDays(i);
//            aWeek[date.getDayOfWeek().getValue() - 1] = date;
//        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        scheduleBinding = FragmentScheduleBinding.inflate(inflater, container, false);
        scheduleContainer = scheduleBinding.schedulesContainer;

        scheduleViewModel.getScheduledRecipes()
                .observe(getViewLifecycleOwner(), (scheduleRecipes -> {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    ScheduleAdapter scheduleAdapter = new ScheduleAdapter(aWeek, scheduleViewModel.getScheduledRecipes().getValue());
                    scheduleAdapter.setOnClickListener(recipeWithScheduledId -> {
                        recipeViewModel.loadRecipeIngredients(recipeWithScheduledId);
                        recipeViewModel.getRecipeIngredients().observe(getViewLifecycleOwner(), ingredients -> {
                            recipeWithScheduledId.recipe.ingredients = ingredients;
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("scheduleRecipe", recipeWithScheduledId);
                            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main2);
                            try {
                                navController.navigate(R.id.navigation_start_cooking, bundle);
                            }catch (RuntimeException e) {
                                Toast.makeText(getContext(), "請再嘗試一次", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                    scheduleContainer.setAdapter(scheduleAdapter);
                }));


        //去除邊框(分隔線為透明色、高度為0)
        scheduleContainer.setDivider(null);
        scheduleContainer.setDividerHeight(0);

        //設定奇偶行數背景顏色
        setListBackgroundColor(scheduleContainer, requireContext());

        addToolbar();

        return scheduleBinding.getRoot();
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
                int itemId = menuItem.getItemId();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main2);
                if (itemId == R.id.foodMenu) {
//                    navController.navigate(R.id.action_navigation_home_to_navigation_camera);
                }
                if (itemId == R.id.editdate) {
                    showDialog();
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.STARTED);
    }

    //彈跳式窗
    private void showDialog() {
        //彈跳頁面
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.schedule_change_display_dialog);

        //取消與確認按鈕
        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        Button confirmButton = dialog.findViewById(R.id.confirm_button);

        // 取消
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 確認
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        scheduleViewModel.loadSchedules();
    }
}