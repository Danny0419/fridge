package com.example.fragment_test.ui.refrigerator;

import android.app.Application;

import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.repository.RefrigeratorIngredientRepository;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class FoodManagementViewModel extends AndroidViewModel {
    private final MutableLiveData<Map<String, List<RefrigeratorIngredient>>> refrigeratorIngredients = new MutableLiveData<>();
    private final RefrigeratorIngredientRepository repository;

    public FoodManagementViewModel(@NonNull Application application) {
        super(application);
        this.repository = RefrigeratorIngredientRepository.getInstance(getApplication());
    }

    public void loadRefrigeratorIngredients() {

        Maybe.fromCallable(repository::getAllIngredients)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<Map<String, List<RefrigeratorIngredient>>>() {
                    @Override
                    public void onSuccess(Map<String, List<RefrigeratorIngredient>> ingredients) {
                        refrigeratorIngredients.setValue(ingredients);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<Map<String, List<RefrigeratorIngredient>>> getRefrigeratorIngredients() {
        return refrigeratorIngredients;
    }

    public void addRefrigeratorIngredients(List<RefrigeratorIngredient> ingredients) {
        Completable.fromAction(() -> repository.buyIngredients(ingredients))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
