package com.example.fragment_test.ui.shopping_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fragment_test.entity.ShoppingIngredient;
import com.example.fragment_test.repository.ShoppingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class ShoppingLIstViewModel extends AndroidViewModel {
    private ShoppingRepository repository;
    private MutableLiveData<List<ShoppingIngredient>> ingredients = new MutableLiveData<>();

    public ShoppingLIstViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ShoppingRepository(application);
    }
    // TODO: Implement the ViewModel

    public MutableLiveData<List<ShoppingIngredient>> loadShoppingList() {
        Maybe.fromCallable(new Callable<List<ShoppingIngredient>>() {
                    @Override
                    public List<ShoppingIngredient> call() throws Exception {
                        return null;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<List<ShoppingIngredient>>() {
                    @Override
                    public void onSuccess(List<ShoppingIngredient> shoppingIngredients) {
                        ingredients.setValue(shoppingIngredients);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return ingredients;
    }
}