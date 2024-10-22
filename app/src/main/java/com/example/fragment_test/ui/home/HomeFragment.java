package com.example.fragment_test.ui.home;

import android.app.Dialog;
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
import com.example.fragment_test.databinding.ScanIngredientConfirmBinding;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.ui.refrigerator.FoodManagementViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private Dialog dialog;
    private ScanIngredientConfirmBinding scanIngredientConfirmBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

//    private void setupDialog(LayoutInflater inflater, ViewGroup container) {
//        scanIngredientConfirmBinding = ScanIngredientConfirmBinding.inflate(inflater, container, false);
//        dialog = new Dialog(getContext());
//        dialog.setContentView(scanIngredientConfirmBinding.getRoot());
//        dialog.setCancelable(false);
//
//        Button continueButton = scanIngredientConfirmBinding.continueButton;
//        Button confirmButton = scanIngredientConfirmBinding.confirmButton;
//
//        continueButton.setOnClickListener(this);
//        confirmButton.setOnClickListener(this);
//
//        // 應急用調整彈跳視窗大小
//        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
//        layoutParams.width = 1000;
//        layoutParams.height = 1020;
//        dialog.getWindow().setAttributes(layoutParams);
//    }

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
                            new RefrigeratorIngredient(0, "牛肉", 500, "牛排照片", "肉類", date, Integer.parseInt(threeDaysLaterStr)),
                            new RefrigeratorIngredient(0, "豬肉", 500, "牛排照片", "肉類", date, Integer.parseInt(threeDaysLaterStr)),
                            new RefrigeratorIngredient(0, "雞肉", 1000, "牛小排照片", "肉類", date, Integer.parseInt(threeDaysLaterStr)),
                            new RefrigeratorIngredient(0, "高麗菜", 100, "牛小排照片", "蔬菜類", date, Integer.parseInt(threeDaysLaterStr)),
                            new RefrigeratorIngredient(0, "青江菜", 200, "牛小排照片", "蔬菜類", date, Integer.parseInt(threeDaysLaterStr)),
                            new RefrigeratorIngredient(0, "洋蔥", 2, "牛小排照片", "蔬菜類", date, Integer.parseInt(threeDaysLaterStr)),
                            new RefrigeratorIngredient(0, "紅蘿波", 300, "牛小排照片", "蔬菜類", date, Integer.parseInt(threeDaysLaterStr)),
                            new RefrigeratorIngredient(0, "玉米", 300, "牛小排照片", "蔬菜類", date, Integer.parseInt(threeDaysLaterStr)),
                            new RefrigeratorIngredient(0, "菠菜", 250, "牛小排照片", "蔬菜類", date, Integer.parseInt(threeDaysLaterStr)),
                            new RefrigeratorIngredient(0, "雞蛋", 10, "牛小排照片", "蛋豆魚肉類", date, Integer.parseInt(threeDaysLaterStr))
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