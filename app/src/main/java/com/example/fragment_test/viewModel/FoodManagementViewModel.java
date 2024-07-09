package com.example.fragment_test.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.repository.FoodManagementRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Handler;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class FoodManagementViewModel extends AndroidViewModel {
    private final MutableLiveData<Map<String, ArrayList<RefrigeratorIngredient>>> refrigeratorIngredients = new MutableLiveData<>();
    private final FoodManagementRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();

    public FoodManagementViewModel(@NonNull Application application) {
        super(application);
        this.repository = new FoodManagementRepository(getApplication());
    }

    public void loadRefrigeratorIngredients() {
        disposable.add(Maybe.fromCallable(new Callable<Map<String, ArrayList<RefrigeratorIngredient>>>() {
                    @Override
                    public Map<String, ArrayList<RefrigeratorIngredient>> call() throws Exception {
                        return repository.getAllIngredients();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableMaybeObserver<Map<String, ArrayList<RefrigeratorIngredient>>>() {
                    @Override
                    public void onSuccess(Map<String, ArrayList<RefrigeratorIngredient>> ingredients) {
                        refrigeratorIngredients.setValue(ingredients);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public MutableLiveData<Map<String, ArrayList<RefrigeratorIngredient>>> getRefrigeratorIngredients() {
        return refrigeratorIngredients;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
