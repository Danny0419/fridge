package com.example.fragment_test.ui.schedule;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fragment_test.entity.ScheduleRecipe;
import com.example.fragment_test.repository.ScheduleRecipeRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class ScheduleViewModel extends AndroidViewModel {
    private final MutableLiveData<Map<DayOfWeek, List<ScheduleRecipe>>> scheduledRecipes = new MutableLiveData<>();
    private final ScheduleRecipeRepository repository;

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        this.repository = ScheduleRecipeRepository.getInstance(getApplication());
    }

    public void loadSchedules() {
        Maybe.fromCallable(repository::getAWeekSchedules)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<Map<DayOfWeek, List<ScheduleRecipe>>>() {
                    @Override
                    public void onSuccess(Map<DayOfWeek, List<ScheduleRecipe>> schedules) {
                        scheduledRecipes.setValue(schedules);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<Map<DayOfWeek, List<ScheduleRecipe>>> getScheduledRecipes() {
        return scheduledRecipes;
    }

}
