package com.example.fragment_test.ui.recipe;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fragment_test.LiveData.SingleLiveData;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeIngredient;
import com.example.fragment_test.entity.RecipeWithScheduledId;
import com.example.fragment_test.entity.Step;
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
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class RecipeViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();
    private final SingleLiveData<List<RecipeIngredient>> recipeIngredients = new SingleLiveData<>();
    private final SingleLiveData<List<Step>> recipeSteps = new SingleLiveData<>();
    private final SingleLiveData<Recipe> recipeDetail = new SingleLiveData<>();
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

    public void loadRecipeSteps(Recipe recipe) {
        recipeRepository.getRecipeSteps(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Step>>() {
                    @Override
                    public void onSuccess(List<Step> steps) {
                        recipeSteps.setValue(steps);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void setRecipesPic(List<Recipe> recipes) {
        Completable.fromAction(() -> recipeRepository.setRecipesPic(recipes))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        RecipeViewModel.this.recipes.setValue(recipes);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void setRecipePic(Recipe recipe) {
        Completable.fromAction(() -> recipeRepository.setRecipePic(recipe))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        RecipeViewModel.this.recipeDetail.setValue(recipe);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public MutableLiveData<List<RecipeIngredient>> getRecipeIngredients() {
        return recipeIngredients;
    }

    public SingleLiveData<List<Step>> getRecipeSteps() {
        return recipeSteps;
    }

    public SingleLiveData<Recipe> getRecipeDetail() {
        return recipeDetail;
    }
}