package com.example.fragment_test.ui.cooking;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.fragment_test.LiveData.SingleLiveData;
import com.example.fragment_test.entity.RecipeWithScheduledId;
import com.example.fragment_test.repository.CookingRepository;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class CookingViewModel extends AndroidViewModel {
    private final SingleLiveData<Boolean> areIngredientSufficient = new SingleLiveData<>();
    private final CookingRepository cookingRepository;
    public CookingViewModel(@NonNull Application application) {
        super(application);
        this.cookingRepository = CookingRepository.getInstance(application);
    }

    public void checkIfIngredientsSufficient(RecipeWithScheduledId recipeWithScheduledId) {
        Maybe.fromCallable(() -> cookingRepository.checkAreIngredientsSufficient(recipeWithScheduledId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        areIngredientSufficient.setValue(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Toast.makeText(getApplication(), "您的食材不夠", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void cooking(RecipeWithScheduledId recipeWithScheduledId) {
        Completable.fromAction(() ->cookingRepository.cooking(recipeWithScheduledId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplication(), "料理完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public SingleLiveData<Boolean> getAreIngredientSufficient() {
        return areIngredientSufficient;
    }
}
