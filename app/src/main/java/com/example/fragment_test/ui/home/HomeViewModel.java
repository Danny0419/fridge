package com.example.fragment_test.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.repository.ScheduleRecipeRepository;
import com.example.fragment_test.repository.ShoppingListIngredientRepository;
import com.example.fragment_test.vo.ShoppingItemVO;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Recipe>> scheduleRecipesOfToday = new MutableLiveData<>();
    private final MutableLiveData<List<ShoppingItemVO>> shoppingList= new MutableLiveData<>();

    private final ScheduleRecipeRepository scheduleRecipeRepository;
    private final ShoppingListIngredientRepository shoppingListIngredientRepository;

    public HomeViewModel(Application application) {
        super(application);
        this.scheduleRecipeRepository = ScheduleRecipeRepository.getInstance(application);
        this.shoppingListIngredientRepository = ShoppingListIngredientRepository.getInstance(application);
    }

    public void loadScheduleRecipesOfToday(int date) {
        Maybe.fromCallable(() ->scheduleRecipeRepository.getSpecificDateScheduledRecipes(date))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<List<Recipe>>() {
                    @Override
                    public void onSuccess(List<Recipe> recipes) {
                        scheduleRecipesOfToday.setValue(recipes);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadShoppingList() {
        Maybe.fromCallable(shoppingListIngredientRepository::getShoppingList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<List<ShoppingItemVO>>() {
                    @Override
                    public void onSuccess(List<ShoppingItemVO> shoppingItemVOS) {
                        shoppingList.setValue(shoppingItemVOS);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<List<Recipe>> getScheduleRecipesOfToday() {
        return scheduleRecipesOfToday;
    }

    public MutableLiveData<List<ShoppingItemVO>> getShoppingList() {
        return shoppingList;
    }
}