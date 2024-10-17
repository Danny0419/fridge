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

import com.example.fragment_test.R;
import com.example.fragment_test.databinding.FragmentHomeBinding;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.ui.refrigerator.FoodManagementViewModel;

import java.time.DayOfWeek;
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
        fragmentOfDate();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//
//        // 观察 Invoice 列表和其项，并更新 UI
//        homeViewModel.getInvoiceWithItemsList().observe(getViewLifecycleOwner(), invoiceWithItemsList -> {
//            StringBuilder allInvoices = new StringBuilder();
//            for (InvoiceWithItems invoiceWithItems : invoiceWithItemsList) {
//                Invoice invoice = invoiceWithItems.invoice;
//                allInvoices.append("\n")
//                        .append("Invoice ID: ").append(invoice.getId()).append("，")
//                        .append("Date: ").append(invoice.getDate()).append("\n");
//
//                // 显示发票下的所有品项
//                for (InvoiceItem item : invoiceWithItems.items) {
//                    allInvoices.append("    Item Name: ").append(item.getName()).append("\n")
//                            .append("    Quantity: ").append(item.getQuantity()).append("，")
//                            .append("    Price: ").append(item.getPrice()).append("\n");
//                }
//            }
//            textView.setText(allInvoices.toString());
//        });
//
        addToolbar();
//
//        binding.testButton.setOnClickListener(this);
//        setupDialog(inflater, container);

        return root;
    }

    private void fragmentOfDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM\n.dd");
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
        return "星期"+ dOWeek;
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
                            new RefrigeratorIngredient(0, "牛小排", 4, "牛小排照片", "肉類", Integer.parseInt(date), Integer.parseInt(threeDaysLaterStr)),
                            new RefrigeratorIngredient(0, "高麗菜", 60, "牛小排照片", "肉類", Integer.parseInt(date), Integer.parseInt(threeDaysLaterStr))
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