package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.ScheduleRecipeDAO;

public class ScheduleRepository {
    private static ScheduleRepository scheduleRepository;
    private final RecipeRepository recipeRepository;
    private final ScheduleRecipeDAO scheduleRecipeDAO;

    private ScheduleRepository(Context context) {
        scheduleRepository = ScheduleRepository.getInstance(context);
        this.scheduleRecipeDAO = FridgeDatabase.getInstance(context).scheduleRecipeDAO();
        this.recipeRepository = RecipeRepository.getInstance(context);
    }

    public static ScheduleRepository getInstance(Context context) {
        if (scheduleRepository == null) {
            scheduleRepository = new ScheduleRepository(context);
        }
        return scheduleRepository;
    }


}
