package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.ScheduleRecipeDAO;
import com.example.fragment_test.entity.PreparedRecipe;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.ScheduleRecipe;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScheduleRecipeRepository {
    private static ScheduleRecipeRepository scheduleRecipeRepository;
    private final ScheduleRecipeDAO scheduleRecipeDAO;
    private final PreparedRecipeRepository preparedRecipeRepository;

    private ScheduleRecipeRepository(Context context) {
        this.scheduleRecipeDAO = FridgeDatabase.getInstance(context).scheduleRecipeDAO();
        this.preparedRecipeRepository = PreparedRecipeRepository.getInstance(context);
    }

    public static ScheduleRecipeRepository getInstance(Context context) {
        if (scheduleRecipeRepository == null) {
            scheduleRecipeRepository = new ScheduleRecipeRepository(context);
        }
        return scheduleRecipeRepository;
    }

    public void schedule(int date, int dayOfWeek, PreparedRecipe preparedRecipe) {
        ScheduleRecipe scheduleRecipe = new ScheduleRecipe(0, preparedRecipe.rId, date, dayOfWeek, 0);
        long scheduleId = scheduleRecipeDAO.insertScheduleRecipe(scheduleRecipe);
        preparedRecipeRepository.schedule((int)scheduleId, preparedRecipe);
    }

    public void finishCooking(List<ScheduleRecipe> scheduleRecipes) {
        scheduleRecipes.forEach(scheduleRecipe -> scheduleRecipe.status = 1);
        scheduleRecipes.forEach(scheduleRecipeDAO::updateScheduleRecipe);
    }

    public boolean checkTodayIsDone(Integer sId) {
        List<ScheduleRecipe> scheduleRecipes = scheduleRecipeDAO.queryIsNotDoneScheduleRecipesByDate(sId);
        return scheduleRecipes.isEmpty();
    }

    public List<Recipe> getSpecificDayScheduleRecipes(int scheduleId) {
        return scheduleRecipeDAO.queryScheduleRecipesByDate(scheduleId);
    }

    public Map<DayOfWeek, List<ScheduleRecipe>> getAWeekSchedules() {
        String today = DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now());
        List<ScheduleRecipe> allNotFinishedSchedule = scheduleRecipeDAO.queryAllNotDoneAndUnexpiredScheduleRecipes(Integer.parseInt(today));
        return allNotFinishedSchedule.stream()
                .collect(Collectors.groupingBy(ScheduleRecipe::getDayOfWeek));
    }

    public List<Recipe> getSpecificDayScheduledRecipes(int date) {
        return scheduleRecipeDAO.queryScheduleRecipesByDate(date);
    }
}
