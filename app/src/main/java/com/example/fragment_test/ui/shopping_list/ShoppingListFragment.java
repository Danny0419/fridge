package com.example.fragment_test.ui.shopping_list;

import static com.example.fragment_test.utils.setListBackground.setListBackgroundColor;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.SwipeController;
import com.example.fragment_test.adapter.ShoppingListAdapter;
import com.example.fragment_test.databinding.FragmentShoppingListBinding;
import com.example.fragment_test.databinding.ShoppinglistAlterDialogBinding;
import com.example.fragment_test.entity.ShoppingIngredient;
import com.example.fragment_test.vo.ShoppingItemVO;

import java.util.List;

public class ShoppingListFragment extends Fragment implements View.OnClickListener {

    private FragmentShoppingListBinding binding;
    private ShoppinglistAlterDialogBinding dialogBinding;
    private ShoppingListViewModel mViewModel;
    private RecyclerView shoppingListItemRecycleView;
    private Dialog dialog;
    private RecyclerView.LayoutManager layoutManager;

    public static ShoppingListFragment newInstance() {
        return new ShoppingListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ShoppingListViewModel.class);
        mViewModel.loadShoppingList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = initialize(inflater, container);
        mViewModel.getCurrShoppingList().observe(getViewLifecycleOwner(), new Observer<List<ShoppingItemVO>>() {
            @Override
            public void onChanged(List<ShoppingItemVO> shoppingIngredients) {
                layoutManager = new LinearLayoutManager(getContext());
                shoppingListItemRecycleView.setLayoutManager(layoutManager);
                ShoppingListAdapter adapter = new ShoppingListAdapter(shoppingIngredients, getContext());
                shoppingListItemRecycleView.setAdapter(adapter);

                // 滑動刪除＆編輯
                SwipeController swipeController = new SwipeController(requireContext());
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
                itemTouchHelper.attachToRecyclerView(shoppingListItemRecycleView);
            }
        });
        // 應急用調整彈跳視窗大小
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = 1000;
        layoutParams.height = 1020;
        dialog.getWindow().setAttributes(layoutParams);


        return view;
    }

    private View initialize(LayoutInflater inflater, ViewGroup container) {

        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        shoppingListItemRecycleView = binding.getRoot().findViewById(R.id.shoppingListItemRecyclerview);
        layoutManager = new LinearLayoutManager(getContext());

        //設定奇偶行數背景顏色
        setListBackgroundColor(shoppingListItemRecycleView, requireContext());

        Button addItemButton = binding.addNewShoppingListItemButton;
        addItemButton.setOnClickListener(this);


        dialogBinding = ShoppinglistAlterDialogBinding.inflate(inflater, container, false);
        Button dialogCancelBtn = dialogBinding.cancelButton;
        Button dialogConfirmBtn = dialogBinding.confirmButton;


        dialog = new Dialog(getContext());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCancelable(false);

        dialogCancelBtn.setOnClickListener(this);
        dialogConfirmBtn.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        int clickedId = v.getId();
        if (clickedId == R.id.addNewShoppingListItemButton) {
            mViewModel.loadAllSortsOfIngredients();
            mViewModel.getAllSorts()
                    .observe(getViewLifecycleOwner(), strings -> {
                        initialSpinner(dialogBinding.sortSpinner, dialogBinding.nameSpinner, strings);
                        dialog.show();
                    });
        } else if (clickedId == R.id.cancel_button) {
            dialogBinding.quantity.setText("");
            dialog.dismiss();

        } else if (clickedId == R.id.confirm_button) {
            try {
                String name = dialogBinding.nameSpinner.getSelectedItem().toString();
                String sort = dialogBinding.sortSpinner.getSelectedItem().toString();
                String quantity = dialogBinding.quantity.getText().toString();
                ShoppingIngredient ingredient = new ShoppingIngredient(0, name, sort, Integer.parseInt(quantity), 0);

                mViewModel.addShoppingItem(ingredient);
                dialogBinding.quantity.setText("");
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "請輸入數量", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void initialSpinner(Spinner sortSpinner, Spinner nameSpinner, List<String> sorts) {
        // Create an ArrayAdapter using the string array and a default sortSpinner layout.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sorts);
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the sortSpinner.
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String sort = adapterView.getSelectedItem().toString();
                mViewModel.loadSortOfIngredientsName(sort);
                mViewModel.getAllSortOfIngredientsName()
                        .observe(getViewLifecycleOwner(), names -> {
                            ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, names);
                            namesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            nameSpinner.setAdapter(namesAdapter);
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}