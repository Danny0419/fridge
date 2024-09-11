package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.ScheduleRecipeDAO;
import com.example.fragment_test.entity.ScheduleRecipe;

import java.util.List;

public class ScheduleRecipeRepository {
    private static ScheduleRecipeRepository scheduleRecipeRepository;
    private final ScheduleRecipeDAO scheduleRecipeDAO;

    private ScheduleRecipeRepository(Context context) {
        this.scheduleRecipeDAO = FridgeDatabase.getInstance(context).scheduleRecipeDAO();
    }

    public static ScheduleRecipeRepository getInstance(Context context) {
        if (scheduleRecipeRepository == null) {
            scheduleRecipeRepository = new ScheduleRecipeRepository(context);
        }
        return scheduleRecipeRepository;
    }

    public long schedule(ScheduleRecipe scheduleRecipe) {
        return scheduleRecipeDAO.insertScheduleRecipe(scheduleRecipe);
    }

    public void finishCooking(List<ScheduleRecipe> scheduleRecipes) {
        scheduleRecipes.forEach(scheduleRecipe -> scheduleRecipe.status = 1);
        scheduleRecipes.forEach(scheduleRecipeDAO::updateScheduleRecipe);
    }

    public boolean checkTodayIsDone(Integer sId) {
        List<ScheduleRecipe> scheduleRecipes = scheduleRecipeDAO.queryIsNotDoneScheduleRecipesBySId(sId);
        return scheduleRecipes.isEmpty();
    }

    public List<ScheduleRecipe> getAllNotFinishedSchedule() {
        return scheduleRecipeDAO.queryAllIsNotDoneScheduleRecipes();
    }
}
