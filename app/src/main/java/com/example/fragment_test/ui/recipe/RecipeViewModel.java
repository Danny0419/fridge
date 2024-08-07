package com.example.fragment_test.ui.recipe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class RecipeViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();
    private final RecipeRepository recipeRepository;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        this.recipeRepository = RecipeRepository.getInstance(getApplication());
    }

    public void loadRecommendRecipes() {
        Maybe.fromCallable(recipeRepository::recommendRecipes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<Optional<List<Recipe>>>() {
                    @Override
                    public void onSuccess(Optional<List<Recipe>> recipes) {
                        recipes.ifPresent(RecipeViewModel.this.recipes::setValue);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<List<Recipe>> getRecipes(){
        return recipes;
    }
}