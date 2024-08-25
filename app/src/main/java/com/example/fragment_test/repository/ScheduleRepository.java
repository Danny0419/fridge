package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.ScheduleDAO;
import com.example.fragment_test.entity.PreparedRecipe;
import com.example.fragment_test.entity.RecipeIngredient;
import com.example.fragment_test.entity.Schedule;
import com.example.fragment_test.entity.ScheduleRecipe;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScheduleRepository {
    private static ScheduleRepository scheduleRepository;
    private final ScheduleRecipeRepository scheduleRecipeRepository;
    private final RefrigeratorIngredientRepository refrigeratorIngredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final ScheduleDAO scheduleDAO;

    private ScheduleRepository(Context context) {
        this.scheduleDAO = FridgeDatabase.getInstance(context).scheduleDAO();
        this.scheduleRecipeRepository = ScheduleRecipeRepository.getInstance(context);
        this.refrigeratorIngredientRepository = RefrigeratorIngredientRepository.getInstance(context);
        this.recipeIngredientRepository =RecipeIngredientRepository.getInstance(context);
    }

    public static ScheduleRepository getInstance(Context context) {
        if (scheduleRepository == null) {
            scheduleRepository = new ScheduleRepository(context);
        }
        return scheduleRepository;
    }


    public void schedule(LocalDate cookingDate, PreparedRecipe preparedRecipe) {
        String date = DateTimeFormatter.BASIC_ISO_DATE.format(cookingDate);
        int scheduleId = Integer.parseInt(date);
        scheduleDAO.insertSchedule(new Schedule(scheduleId, cookingDate.getDayOfWeek().getValue(), 0));
        ScheduleRecipe scheduleRecipe = new ScheduleRecipe(0, preparedRecipe.rId, scheduleId, cookingDate.getDayOfWeek().getValue(),0);
        scheduleRecipeRepository.schedule(scheduleRecipe);
    }

    public void cooking(List<ScheduleRecipe> scheduleRecipes) {
        scheduleRecipeRepository.finishCooking(scheduleRecipes);
        Integer sId = scheduleRecipes.get(0).sId;
        boolean isTodayDone = scheduleRecipeRepository.checkTodayIsDone(sId);
        if (isTodayDone) {
            scheduleDAO.updateSchedule(sId);
        }

        List<RecipeIngredient> recipesUsingIngredient = recipeIngredientRepository.getRecipesUsingIngredient(scheduleRecipes);

        refrigeratorIngredientRepository.consumeIngredients(recipesUsingIngredient);
    }

    public Map<DayOfWeek, List<ScheduleRecipe>> getAWeekSchedules() {
        List<ScheduleRecipe> allNotFinishedSchedule = scheduleRecipeRepository.getAllNotFinishedSchedule();
        return allNotFinishedSchedule.stream()
                .collect(Collectors.groupingBy(ScheduleRecipe::getDayOfWeek));
    }
}
