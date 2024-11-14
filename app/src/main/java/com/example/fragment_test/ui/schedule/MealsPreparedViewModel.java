package com.example.fragment_test.ui.schedule;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fragment_test.LiveData.SingleLiveData;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeWithPreRecipeId;
import com.example.fragment_test.repository.PreparedRecipeRepository;
import com.example.fragment_test.repository.RecipeRepository;
import com.example.fragment_test.repository.ScheduleRecipeRepository;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class MealsPreparedViewModel extends AndroidViewModel {
    private final SingleLiveData<List<Recipe>> scheduledRecipes = new SingleLiveData<>();
    private final SingleLiveData<List<RecipeWithPreRecipeId>> preparedRecipes = new SingleLiveData<>();
    private final ScheduleRecipeRepository scheduleRecipeRepository;
    private final PreparedRecipeRepository preparedRecipeRepository;
    private final RecipeRepository recipeRepository;


    public MealsPreparedViewModel(@NonNull Application application) {
        super(application);
        this.scheduleRecipeRepository = ScheduleRecipeRepository.getInstance(application);
        this.preparedRecipeRepository = PreparedRecipeRepository.getInstance(application);
        this.recipeRepository = RecipeRepository.getInstance(application);
    }

    public void loadSchedules(int date) {
        Maybe.fromCallable(new Callable<List<Recipe>>() {
                    @Override
                    public List<Recipe> call() throws Exception {
                        List<Recipe> recipes = scheduleRecipeRepository.getSpecificDateScheduledRecipes(date);
                        recipeRepository.loadRecipesPic(recipes);
                        return recipes;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<List<Recipe>>() {
                    @Override
                    public void onSuccess(List<Recipe> recipes) {
                        scheduledRecipes.setValue(recipes);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadPreparedRecipes() {
        Maybe.fromCallable(new Callable<List<RecipeWithPreRecipeId>>() {
                    @Override
                    public List<RecipeWithPreRecipeId> call() throws Exception {
                        List<RecipeWithPreRecipeId> recipes = preparedRecipeRepository.getPreparedRecipes();
                        List<Recipe> rs = recipes.stream()
                                        .map(o -> o.recipe)
                                                .toList();
                        recipeRepository.loadRecipesPic(rs);
                        return recipes;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<List<RecipeWithPreRecipeId>>() {
                    @Override
                    public void onSuccess(List<RecipeWithPreRecipeId> recipes) {
                        preparedRecipes.setValue(recipes);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void schedule(int date, int dayOfWeek, RecipeWithPreRecipeId recipe) {
        Completable.fromAction(() -> scheduleRecipeRepository.schedule(date, dayOfWeek, recipe))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        loadSchedules(date);
                        loadPreparedRecipes();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void unSchedule(int date, Recipe recipe) {
        Completable.fromAction(() -> scheduleRecipeRepository.unSchedule(date, recipe))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        loadSchedules(date);
                        loadPreparedRecipes();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public SingleLiveData<List<Recipe>> getScheduledRecipes() {
        return scheduledRecipes;
    }

    public SingleLiveData<List<RecipeWithPreRecipeId>> getPreparedRecipes() {
        return preparedRecipes;
    }
}
