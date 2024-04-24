package com.example.fragment_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fragment_test.fragments.ShoppingListFragment;
import com.example.fragment_test.pojo.Ingredient;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        ArrayList<Ingredient> shoppingList = new ArrayList<>();
        shoppingList.add(new Ingredient(0,"你好","無","於","2",1));
        shoppingList.add(new Ingredient(2,"你好","無","於","5",0));
        ShoppingListFragment shoppingListFrag = (ShoppingListFragment) getSupportFragmentManager().findFragmentById(R.id.shoppingListFrag);
        shoppingListFrag.setShoppingList(shoppingList);

    }
}