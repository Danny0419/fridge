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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.ShoppingListAdapter;
import com.example.fragment_test.databinding.FragmentShoppingListBinding;
import com.example.fragment_test.databinding.ShoppingItemEditDialogBinding;
import com.example.fragment_test.databinding.ShoppinglistAlterDialogBinding;
import com.example.fragment_test.entity.ShoppingIngredient;
import com.example.fragment_test.vo.ShoppingItemVO;

import java.util.List;

public class ShoppingListFragment extends Fragment implements View.OnClickListener {

    private FragmentShoppingListBinding binding;
    private ShoppinglistAlterDialogBinding dialogBinding;
    private ShoppingItemEditDialogBinding shoppingItemEditDialogBinding;
    private ShoppingListViewModel mViewModel;
    private RecyclerView shoppingListItemRecycleView;
    private Dialog addItemDialog;
    private Dialog editItemDialog;
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

        // 應急用調整彈跳視窗大小


        mViewModel.getCurrShoppingList().observe(getViewLifecycleOwner(), new Observer<List<ShoppingItemVO>>() {
            @Override
            public void onChanged(List<ShoppingItemVO> shoppingIngredients) {
                layoutManager = new LinearLayoutManager(getContext());
                shoppingListItemRecycleView.setLayoutManager(layoutManager);
                ShoppingListAdapter adapter = new ShoppingListAdapter(shoppingIngredients);
                adapter.setShoppingItemEditedListener(shoppingItem -> {
                    setShoppingEditDialogAttribute(shoppingItem, shoppingItemEditDialogBinding);
                    editItemDialog.show();

                    setShoppingEditDialogBtnOnClickListener(shoppingItem, shoppingItemEditDialogBinding);
                });
                shoppingListItemRecycleView.setAdapter(adapter);


            }
        });
        return view;
    }

    private void setShoppingEditDialogBtnOnClickListener(ShoppingItemVO shoppingItemVO, ShoppingItemEditDialogBinding shoppingItemEditDialogBinding) {
        shoppingItemEditDialogBinding.cancelButton.setOnClickListener(view -> editItemDialog.dismiss());

        shoppingItemEditDialogBinding.editButton.setOnClickListener(view -> {
            mViewModel.editShoppingItem(shoppingItemVO, Integer.parseInt(shoppingItemEditDialogBinding.quantity.getText().toString()));
            editItemDialog.dismiss();
        });

        shoppingItemEditDialogBinding.deleteButton.setOnClickListener(view -> {
            mViewModel.deleteShoppingItem(shoppingItemVO);
            editItemDialog.dismiss();
        });
    }

    private void setShoppingEditDialogAttribute(ShoppingItemVO shoppingItem, ShoppingItemEditDialogBinding shoppingItemEditDialogBinding) {
        shoppingItemEditDialogBinding.name.setText(shoppingItem.name);
        shoppingItemEditDialogBinding.sort.setText(shoppingItem.sort);
        shoppingItemEditDialogBinding.quantity.setText(shoppingItem.sumOfQuantity + "");
    }

    private View initialize(LayoutInflater inflater, ViewGroup container) {

        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        shoppingItemEditDialogBinding = ShoppingItemEditDialogBinding.inflate(getLayoutInflater());
        shoppingListItemRecycleView = binding.getRoot().findViewById(R.id.shoppingListItemRecyclerview);
        layoutManager = new LinearLayoutManager(getContext());

        //設定奇偶行數背景顏色
        setListBackgroundColor(shoppingListItemRecycleView, requireContext());

        Button addItemButton = binding.addNewShoppingListItemButton;
        addItemButton.setOnClickListener(this);


        dialogBinding = ShoppinglistAlterDialogBinding.inflate(inflater, container, false);
        Button dialogCancelBtn = dialogBinding.cancelButton;
        Button dialogConfirmBtn = dialogBinding.confirmButton;

        addItemDialog = new Dialog(getContext());
        addItemDialog.setContentView(dialogBinding.getRoot());
        addItemDialog.setCancelable(false);
        WindowManager.LayoutParams layoutParams = addItemDialog.getWindow().getAttributes();
        layoutParams.width = 1000;
        layoutParams.height = 1020;
        addItemDialog.getWindow().setAttributes(layoutParams);

        editItemDialog = new Dialog(getContext());
        editItemDialog.setContentView(shoppingItemEditDialogBinding.getRoot());
        editItemDialog.setCancelable(false);
        WindowManager.LayoutParams layoutParams1 = editItemDialog.getWindow().getAttributes();
//        layoutParams1.width = 1000;
//        layoutParams1.height = 1020;
        editItemDialog.getWindow().setAttributes(layoutParams);


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
                        addItemDialog.show();
                    });
        } else if (clickedId == R.id.cancel_button) {
            dialogBinding.quantity.setText("");
            addItemDialog.dismiss();

        } else if (clickedId == R.id.confirm_button) {
            try {
                String name = dialogBinding.nameSpinner.getSelectedItem().toString();
                String sort = dialogBinding.sortSpinner.getSelectedItem().toString();
                String quantity = dialogBinding.quantity.getText().toString();
                ShoppingIngredient ingredient = new ShoppingIngredient(0, name, sort, Integer.parseInt(quantity), 0);

                mViewModel.addShoppingItem(ingredient);
                dialogBinding.quantity.setText("");
                addItemDialog.dismiss();
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