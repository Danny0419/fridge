package com.example.fragment_test.ui.schedule;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fragment_test.entity.ScheduleRecipe;
import com.example.fragment_test.repository.ScheduleRepository;

import java.util.List;
import java.util.Map;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class ScheduleViewModel extends AndroidViewModel {
    private final MutableLiveData<Map<Integer, List<ScheduleRecipe>>> scheduleRecipes = new MutableLiveData<>();
    private final ScheduleRepository repository;

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        this.repository = ScheduleRepository.getInstance(getApplication());
    }

    public void loadSchedules() {

        Maybe.fromCallable(repository::getAWeekSchedules)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<Map<Integer, List<ScheduleRecipe>>>() {
                    @Override
                    public void onSuccess(Map<Integer, List<ScheduleRecipe>> ingredients) {
                        scheduleRecipes.setValue(ingredients);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<Map<Integer, List<ScheduleRecipe>>> getScheduleRecipes() {
        return scheduleRecipes;
    }

}
