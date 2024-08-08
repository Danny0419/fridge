package com.example.fragment_test.ui.shopping_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fragment_test.entity.ShoppingIngredient;
import com.example.fragment_test.repository.ShoppingListIngredientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class ShoppingListViewModel extends AndroidViewModel {
    private ShoppingListIngredientRepository repository;
    private MutableLiveData<List<ShoppingIngredient>> currShoppingList;

    public ShoppingListViewModel(@NonNull Application application) {
        super(application);
        this.repository = ShoppingListIngredientRepository.getInstance(application);
    }
    // TODO: Implement the ViewModel

    public void loadShoppingList() {
        Maybe.fromCallable(() -> Optional.ofNullable(repository.getShoppingList())
                        .orElse(new ArrayList<>())
                ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<List<ShoppingIngredient>>() {
                    @Override
                    public void onSuccess(List<ShoppingIngredient> shoppingIngredients) {
                        currShoppingList.setValue(shoppingIngredients);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void addShoppingItem(ShoppingIngredient ingredient) {

        Maybe.fromCallable(() -> Optional.ofNullable(repository.addShoppingItem(ingredient))
                        .orElse(new ArrayList<>())
                ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<List<ShoppingIngredient>>() {
                    @Override
                    public void onSuccess(List<ShoppingIngredient> shoppingList) {
                        currShoppingList.setValue(shoppingList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public MutableLiveData<List<ShoppingIngredient>> getCurrShoppingList() {
        if (currShoppingList == null) {
            currShoppingList = new MutableLiveData<>();
        }
        return currShoppingList;
    }
}