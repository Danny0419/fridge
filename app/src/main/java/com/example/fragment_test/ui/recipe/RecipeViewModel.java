package com.example.fragment_test.ui.recipe;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeIngredient;
import com.example.fragment_test.entity.RecipeWithScheduledId;
import com.example.fragment_test.repository.PreparedRecipeRepository;
import com.example.fragment_test.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class RecipeViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();
    private final MutableLiveData<List<RecipeIngredient>> recipeIngredients = new MutableLiveData<>();
    private final RecipeRepository recipeRepository;
    private final PreparedRecipeRepository preparedRecipeRepository;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        this.recipeRepository = RecipeRepository.getInstance(application);
        this.preparedRecipeRepository = PreparedRecipeRepository.getInstance(application);
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

    public void addInterestingRecipe(Recipe recipe) {
        Completable.fromAction(() -> preparedRecipeRepository.addInterestingRecipe(recipe))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplication(), "添加成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void collectAndUnCollectRecipe(Recipe recipe) {
        if (recipe.collected == 0) {
            Completable.fromAction(() -> recipeRepository.collectRecipe(recipe)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            Toast.makeText(getApplication(), "已收藏", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });

        } else {
            Completable.fromAction(() -> recipeRepository.unCollectRecipe(recipe)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            Toast.makeText(getApplication(), "取消收藏", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }


    }

    public void loadRecipeIngredients(RecipeWithScheduledId recipeWithScheduledId) {
        Maybe.fromCallable(new Callable<List<RecipeIngredient>>() {
                    @Override
                    public List<RecipeIngredient> call() throws Exception {
                        return recipeRepository.getRecipeIngredients(recipeWithScheduledId.recipe.id);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<List<RecipeIngredient>>() {
                    @Override
                    public void onSuccess(List<RecipeIngredient> ingredients) {
                        recipeIngredients.setValue(ingredients);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void showCollectionRecipe() {
        Maybe.fromCallable(recipeRepository::showRecipeCollection)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<List<Recipe>>() {
                    @Override
                    public void onSuccess(List<Recipe> collectedRecipe) {
                        recipes.setValue(collectedRecipe);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public MutableLiveData<List<RecipeIngredient>> getRecipeIngredients() {
        return recipeIngredients;
    }
}