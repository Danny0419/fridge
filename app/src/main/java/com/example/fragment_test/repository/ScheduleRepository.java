package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.ScheduleDAO;
import com.example.fragment_test.entity.PreparedRecipe;
import com.example.fragment_test.entity.Schedule;
import com.example.fragment_test.entity.ScheduleRecipe;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ScheduleRepository {
    private static ScheduleRepository scheduleRepository;
    private final ScheduleRecipeRepository scheduleRecipeRepository;
    private final ScheduleDAO scheduleDAO;

    private ScheduleRepository(Context context) {
        this.scheduleDAO = FridgeDatabase.getInstance(context).scheduleDAO();
        this.scheduleRecipeRepository = ScheduleRecipeRepository.getInstance(context);
    }

    public static ScheduleRepository getInstance(Context context) {
        if (scheduleRepository == null) {
            scheduleRepository = new ScheduleRepository(context);
        }
        return scheduleRepository;
    }


    public void schedule(LocalDate day, PreparedRecipe preparedRecipe) {
        String date = DateTimeFormatter.BASIC_ISO_DATE.format(day);
        int scheduleId = Integer.parseInt(date);
        scheduleDAO.insertSchedule(new Schedule(scheduleId, day.getDayOfWeek().getValue(), 0));
        ScheduleRecipe scheduleRecipe = new ScheduleRecipe(0, preparedRecipe.rId, scheduleId, 0);
        scheduleRecipeRepository.schedule(scheduleRecipe);
    }
}
