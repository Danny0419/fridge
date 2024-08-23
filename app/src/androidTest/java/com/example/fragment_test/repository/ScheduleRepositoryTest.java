package com.example.fragment_test.repository;

import androidx.room.ExperimentalRoomApi;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RecipeDAO;
import com.example.fragment_test.database.RecipeIngredientDAO;
import com.example.fragment_test.database.RefrigeratorIngredientDAO;
import com.example.fragment_test.database.ScheduleDAO;
import com.example.fragment_test.database.ScheduleRecipeDAO;
import com.example.fragment_test.entity.PreparedRecipe;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeIngredient;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.entity.Schedule;
import com.example.fragment_test.entity.ScheduleRecipe;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@ExperimentalRoomApi
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ScheduleRepositoryTest extends TestCase {
    public FridgeDatabase database;
    public ScheduleRepository scheduleRepository;
    public RecipeDAO recipeDAO;
    public ScheduleDAO scheduleDAO;
    public ScheduleRecipeDAO scheduleRecipeDAO;
    public RecipeIngredientDAO recipeIngredientDAO;
    public RefrigeratorIngredientDAO refrigeratorIngredientDAO;

    @Test
    public void getAWeekSchedules() {
        Recipe recipe = new Recipe(0, "牛肉炒飯", "照片", 1, 0);
        Recipe recipe1 = new Recipe(0, "豬肉炒飯", "照片", 1, 0);
        recipeDAO.insertRecipe(recipe);
        recipeDAO.insertRecipe(recipe1);
        PreparedRecipe preparedRecipe = new PreparedRecipe(0, 1);
        PreparedRecipe preparedRecipe1 = new PreparedRecipe(0, 2);
        scheduleRepository.schedule(LocalDate.now(),preparedRecipe);
        scheduleRepository.schedule(LocalDate.now().plusDays(1),preparedRecipe1);

        Schedule schedule = scheduleDAO.getSchedule(20240821);
        assertEquals(20240821, schedule.date);

        Schedule schedule1 = scheduleDAO.getSchedule(20240822);
        assertEquals(20240822, schedule1.date);

        Map<Integer, List<ScheduleRecipe>> aWeekSchedules = scheduleRepository.getAWeekSchedules();
        assertEquals(2, aWeekSchedules.size());

        List<ScheduleRecipe> scheduleRecipes = aWeekSchedules.get(20240821);
        assertEquals(1, scheduleRecipes.size());

        List<ScheduleRecipe> scheduleRecipes1 = aWeekSchedules.get(20240822);
        assertEquals(1, scheduleRecipes1.size());

    }

    @Test
    public void cooking() {
        Recipe recipe = new Recipe(0, "牛肉炒飯", "照片", 1, 0);
        Recipe recipe1 = new Recipe(0, "豬肉炒飯", "照片", 1, 0);
        recipeDAO.insertRecipe(recipe);
        recipeDAO.insertRecipe(recipe1);

        RecipeIngredient recipeIngredient = new RecipeIngredient(0, "牛肉", 3, null, 1);
        RecipeIngredient recipeIngredient1 = new RecipeIngredient(0, "豬肉", 2, null, 2);
        recipeIngredientDAO.insertRecipeIngredient(recipeIngredient);
        recipeIngredientDAO.insertRecipeIngredient(recipeIngredient1);

        RefrigeratorIngredient refrigeratorIngredient = new RefrigeratorIngredient("牛肉", "牛肉", 4, null, 0, null, 0);
        RefrigeratorIngredient refrigeratorIngredient1 = new RefrigeratorIngredient("豬肉", "豬肉", 4, null, 0, null, 0);
        refrigeratorIngredientDAO.insertIngredient(refrigeratorIngredient);
        refrigeratorIngredientDAO.insertIngredient(refrigeratorIngredient1);

        PreparedRecipe preparedRecipe = new PreparedRecipe(0, 1);
        PreparedRecipe preparedRecipe1 = new PreparedRecipe(0, 2);
        scheduleRepository.schedule(LocalDate.now(),preparedRecipe);
        scheduleRepository.schedule(LocalDate.now(),preparedRecipe1);

        Schedule schedule = scheduleDAO.getSchedule(20240823);
        assertEquals(20240823, schedule.date);

        List<ScheduleRecipe> scheduleRecipes = scheduleRecipeDAO.queryScheduleRecipesBySId(20240823);

        scheduleRepository.cooking(scheduleRecipes);

        Schedule scheduleStatusIsZero = scheduleDAO.getScheduleStatusIsZero(20240823);
        assertNull(scheduleStatusIsZero);

        List<RefrigeratorIngredient> allRefrigeratorIngredients = refrigeratorIngredientDAO.getAllRefrigeratorIngredients();
        assertEquals(4, allRefrigeratorIngredients.size());
    }

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
        recipeIngredientDAO = FridgeDatabase.getInstance(ApplicationProvider.getApplicationContext()).recipeIngredientDAO();
        refrigeratorIngredientDAO = FridgeDatabase.getInstance(ApplicationProvider.getApplicationContext()).refrigeratorDAO();
    }
}