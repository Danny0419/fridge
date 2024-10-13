package com.example.fragment_test.ui.shopping_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fragment_test.entity.ShoppingIngredient;
import com.example.fragment_test.repository.ShoppingListIngredientRepository;
import com.example.fragment_test.vo.ShoppingItemVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ShoppingListViewModel extends AndroidViewModel {
    private ShoppingListIngredientRepository repository;
    private MutableLiveData<List<ShoppingItemVO>> currShoppingList = new MutableLiveData<>();
    private MutableLiveData<List<String>> allSorts =new MutableLiveData<>();
    private MutableLiveData<List<String>> allSortOfIngredientsName =new MutableLiveData<>();

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
                .subscribe(new DisposableMaybeObserver<List<ShoppingItemVO>>() {
                    @Override
                    public void onSuccess(List<ShoppingItemVO> shoppingIngredients) {
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
                        loadShoppingList();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public MutableLiveData<List<ShoppingItemVO>> getCurrShoppingList() {
        return currShoppingList;
    }

    public MutableLiveData<List<String>> getAllSorts() {
        return allSorts;
    }

    public void loadAllSortsOfIngredients() {
        repository.getAllSortsOfIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<String>>() {
                    @Override
                    public void onSuccess(List<String> strings) {
                        allSorts.setValue(strings);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void loadSortOfIngredientsName(String sort) {
        repository.getSortOfIngredientsName(sort)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<String>>() {
                    @Override
                    public void onSuccess(List<String> names) {
                        allSortOfIngredientsName.setValue(names);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public MutableLiveData<List<String>> getAllSortOfIngredientsName() {
        return allSortOfIngredientsName;
    }
}