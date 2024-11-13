package com.example.fragment_test.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.fragment_test.adapter.ExpiringRefrigeratorIngredientsAdapterForHome;
import com.example.fragment_test.adapter.RecipeAdapterForHome;
import com.example.fragment_test.adapter.ShoppingListAdapterForHome;
import com.example.fragment_test.databinding.FragmentHomeBinding;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.ui.refrigerator.FoodManagementViewModel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        LocalDate now = LocalDate.now();

        fragmentOfDate(now);

        homeViewModel.loadScheduleRecipesOfToday(Integer.parseInt(DateTimeFormatter.BASIC_ISO_DATE.format(now)));
        homeViewModel.getScheduleRecipesOfToday()
                .observe(getViewLifecycleOwner(), (recipes -> {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    binding.eatingForToday.setLayoutManager(layoutManager);
                    binding.eatingForToday.setAdapter(new RecipeAdapterForHome(recipes));
                }));

        homeViewModel.loadShoppingList();
        homeViewModel.getShoppingList()
                .observe(getViewLifecycleOwner(), (shoppingItemVOS) -> {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    binding.shoppingListRecycle.setLayoutManager(layoutManager);
                    binding.shoppingListRecycle.setAdapter(new ShoppingListAdapterForHome(shoppingItemVOS));
                });

        homeViewModel.loadExpiringRefrigeratorIngredient();
        homeViewModel.getExpiringIngredients()
                .observe(getViewLifecycleOwner(), (ingredientDetailVOList) -> {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    binding.expiringIngredients.setLayoutManager(layoutManager);
                    binding.expiringIngredients.setAdapter(new ExpiringRefrigeratorIngredientsAdapterForHome(ingredientDetailVOList));
                });

        addToolbar();

        return binding.getRoot();
    }

    private void fragmentOfDate(LocalDate now) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.\nMM.dd.");
        String dateStr = dateTimeFormatter.format(now);
        binding.date.setText(dateStr);
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        binding.weekDate.setText(convertDayOfWeekToChiStr(dayOfWeek));
    }

    public String convertDayOfWeekToChiStr(DayOfWeek dayOfWeek) {
        String dOWeek = "一";
        switch (dayOfWeek) {
            case TUESDAY -> dOWeek = "二";
            case WEDNESDAY -> dOWeek = "三";
            case THURSDAY -> dOWeek = "四";
            case FRIDAY -> dOWeek = "五";
            case SATURDAY -> dOWeek = "六";
            case SUNDAY -> dOWeek = "日";
        }
        return "星期" + dOWeek;
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
                            new RefrigeratorIngredient(1, "豬五花肉", 500, "porkpieces", "豬肉", Integer.parseInt(date),someDaysLater(3)),
                            new RefrigeratorIngredient(2, "火鍋牛肉片", 500, "hotpotslicedbeef", "牛肉", Integer.parseInt(date),someDaysLater(3)),
                            new RefrigeratorIngredient(3, "雞腿肉", 500, "chickenthigh", "禽類", Integer.parseInt(date),someDaysLater(3)),
                            new RefrigeratorIngredient(4, "高麗菜", 100, "cabbage", "葉菜花菜", Integer.parseInt(date),someDaysLater(7)),
                            new RefrigeratorIngredient(5, "青江菜", 200, "spooncabbage", "葉菜花菜", Integer.parseInt(date),someDaysLater(5)),
                            new RefrigeratorIngredient(6, "洋蔥", 200, "onion", "根莖類", Integer.parseInt(date),someDaysLater(14)),
                            new RefrigeratorIngredient(7, "紅蘿蔔", 300, "carrot", "根莖類", Integer.parseInt(date),someDaysLater(14)),
                            new RefrigeratorIngredient(8, "白蘿蔔", 300, "whiteradish", "根莖類", Integer.parseInt(date),someDaysLater(10)),
                            new RefrigeratorIngredient(9, "玉米", 300, "corn", "蔬菜類", Integer.parseInt(date),someDaysLater(7)),
                            new RefrigeratorIngredient(10, "菠菜", 250, "spinach", "葉菜花菜", Integer.parseInt(date),someDaysLater(4)),
                            new RefrigeratorIngredient(11, "蛋", 10, "egg", "禽類", Integer.parseInt(date),someDaysLater(21)),
                            new RefrigeratorIngredient(12, "花椰菜", 100, "broccoli", "葉菜花菜", Integer.parseInt(date),someDaysLater(5)),
                            new RefrigeratorIngredient(13, "番茄", 100, "tomato", "葉菜花菜", Integer.parseInt(date),someDaysLater(5)),
                            new RefrigeratorIngredient(14, "小黃瓜", 250, "cucumber", "豆菜與瓜菜", Integer.parseInt(date),someDaysLater(5)),
                            new RefrigeratorIngredient(15, "蘑菇", 250, "mushroom", "蕈菇類", Integer.parseInt(date),someDaysLater(5))
                    );
                    foodManagementViewModel.addRefrigeratorIngredients(ingredients);
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.STARTED);
    }

    public Integer someDaysLater(int day){
        LocalDate now = LocalDate.now();
        String plusDay = DateTimeFormatter.BASIC_ISO_DATE.format(now.plusDays(day));
        return Integer.parseInt(plusDay);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onClick(View v) {
        int clickedId = v.getId();

//        if (clickedId == R.id.test_button) {
//            dialog.show();
//
//            //把checkbox改成textview
//            RecyclerView recyclerView = scanIngredientConfirmBinding.ingredientList.shoppingListItemRecyclerview;
//            for (int i = 0; i < recyclerView.getChildCount(); i++) {
//                View view = recyclerView.getChildAt(i);
//                CheckBox checkBox = view.findViewById(R.id.shoppingItemState);
//                if (checkBox != null) {
//                    // textview
//                    TextView textView = new TextView(getContext());
//                    textView.setTextSize(20); //字體大小
//
//                    //替換checkbox
//                    ViewGroup parent = (ViewGroup) checkBox.getParent();
//                    int index = parent.indexOfChild(checkBox);
//                    parent.removeView(checkBox);
//                    parent.addView(textView, index);
//                }
//            }
//
//        } else if (clickedId == R.id.continue_button) {
//            dialog.dismiss();
//        } else if (clickedId == R.id.confirm_button){
//            dialog.dismiss();
//        }
    }
}