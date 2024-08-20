package com.example.fragment_test.repository;

import androidx.room.ExperimentalRoomApi;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RecipeDAO;
import com.example.fragment_test.database.ScheduleDAO;
import com.example.fragment_test.database.ScheduleRecipeDAO;
import com.example.fragment_test.entity.PreparedRecipe;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.Schedule;
import com.example.fragment_test.entity.ScheduleRecipe;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.List;

@ExperimentalRoomApi
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ScheduleRepositoryTest extends TestCase {
    public FridgeDatabase database;
    public ScheduleRepository scheduleRepository;
    public RecipeDAO recipeDAO;
    public ScheduleDAO scheduleDAO;
    public ScheduleRecipeDAO scheduleRecipeDAO;

    @Test
    public void Schedule() {
        Recipe recipe = new Recipe(0, "牛肉炒飯", "照片", 1, 0);
        Recipe recipe1 = new Recipe(0, "豬肉炒飯", "照片", 1, 0);
        recipeDAO.insertRecipe(recipe);
        recipeDAO.insertRecipe(recipe1);
        PreparedRecipe preparedRecipe = new PreparedRecipe(0, 1);
        PreparedRecipe preparedRecipe1 = new PreparedRecipe(0, 2);
        scheduleRepository.schedule(LocalDate.now(),preparedRecipe);
        scheduleRepository.schedule(LocalDate.now(),preparedRecipe1);

        Schedule schedule = scheduleDAO.getSchedule(20240820);
        assertEquals(20240820, schedule.date);

        List<ScheduleRecipe> scheduleRecipes = scheduleRecipeDAO.queryScheduleRecipesBySId(20240820);
        assertEquals(2, scheduleRecipes.size());
        assertEquals(20240820, scheduleRecipes.get(0).sId.intValue());
        assertEquals(20240820, scheduleRecipes.get(1).sId.intValue());
    }

    @Before
    public void setUp() {
        scheduleRepository = ScheduleRepository.getInstance(ApplicationProvider.getApplicationContext());
        recipeDAO = FridgeDatabase.getInstance(ApplicationProvider.getApplicationContext()).recipeDAO();
        scheduleDAO = FridgeDatabase.getInstance(ApplicationProvider.getApplicationContext()).scheduleDAO();
        scheduleRecipeDAO = FridgeDatabase.getInstance(ApplicationProvider.getApplicationContext()).scheduleRecipeDAO();
    }
}