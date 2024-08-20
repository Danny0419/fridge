package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.ScheduleRecipeDAO;
import com.example.fragment_test.entity.ScheduleRecipe;

public class ScheduleRecipeRepository {
    private static ScheduleRecipeRepository scheduleRecipeRepository;
    private final ScheduleRecipeDAO scheduleRecipeDAO;
    private final RecipeRepository recipeRepository;

    private ScheduleRecipeRepository(Context context) {
        this.scheduleRecipeDAO = FridgeDatabase.getInstance(context).scheduleRecipeDAO();
        this.recipeRepository = RecipeRepository.getInstance(context);
    }

    public static ScheduleRecipeRepository getInstance(Context context) {
        if (scheduleRecipeRepository == null) {
            scheduleRecipeRepository = new ScheduleRecipeRepository(context);
        }
        return scheduleRecipeRepository;
    }

    public void schedule(ScheduleRecipe scheduleRecipe) {
        scheduleRecipeDAO.insertScheduleRecipe(scheduleRecipe);
    }
}
