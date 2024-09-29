package com.example.fragment_test.ui.schedule;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeWithPreRecipeId;
import com.example.fragment_test.repository.PreparedRecipeRepository;
import com.example.fragment_test.repository.ScheduleRecipeRepository;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class MealsPreparedViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Recipe>> scheduledRecipes = new MutableLiveData<>();
    private final MutableLiveData<List<RecipeWithPreRecipeId>> preparedRecipes = new MutableLiveData<>();
    private final ScheduleRecipeRepository scheduleRecipeRepository;
    private final PreparedRecipeRepository preparedRecipeRepository;

    public MealsPreparedViewModel(@NonNull Application application) {
        super(application);
        this.scheduleRecipeRepository = ScheduleRecipeRepository.getInstance(application);
        this.preparedRecipeRepository = PreparedRecipeRepository.getInstance(application);
    }

    public void loadSchedules(int scheduleId) {
        Maybe.fromCallable(new Callable<List<Recipe>>() {
                    @Override
                    public List<Recipe> call() throws Exception {
                        return scheduleRecipeRepository.getSpecificDayScheduledRecipes(scheduleId);
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
                        return preparedRecipeRepository.getPreparedRecipes();
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
//        Completable.fromAction(() -> scheduleRepository.schedule(date, dayOfWeek, new PreparedRecipe()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableCompletableObserver() {
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//                });
    }

    public MutableLiveData<List<Recipe>> getScheduledRecipes() {
        return scheduledRecipes;
    }

    public MutableLiveData<List<RecipeWithPreRecipeId>> getPreparedRecipes() {
        return preparedRecipes;
    }
}
