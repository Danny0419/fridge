package com.example.fragment_test.database;

import androidx.room.ExperimentalRoomApi;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.ScheduleRecipe;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@ExperimentalRoomApi
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ScheduleRecipeDAOTest extends TestCase {
    public FridgeDatabase database;
    public ScheduleRecipeDAO scheduleRecipeDAO;
    public RecipeDAO recipeDAO;
    public ScheduleDAO scheduleDAO;

    @Test
    public void updateScheduleRecipeStatus(){
        Recipe recipe = new Recipe(0, "炒蛋", "炒蛋照片", 2, 0);
        Recipe recipe1 = new Recipe(0, "炒麵", "炒麵照片", 2, 0);
        recipeDAO.insertRecipe(recipe);
        recipeDAO.insertRecipe(recipe1);

        Schedule schedule = new Schedule(0, 0, 0);
        scheduleDAO.insertSchedule(schedule);
        ScheduleRecipe scheduleRecipe = new ScheduleRecipe(0, 1, 1, 1,0);
        ScheduleRecipe scheduleRecipe1 = new ScheduleRecipe(0, 2, 1, 1,0);
        long l = scheduleRecipeDAO.insertScheduleRecipe(scheduleRecipe);
        long l1 = scheduleRecipeDAO.insertScheduleRecipe(scheduleRecipe1);
        assertEquals(1, l);
        assertEquals(2, l1);
        List<ScheduleRecipe> scheduleRecipes = scheduleRecipeDAO.queryIsNotDoneScheduleRecipesByDate(1);
        assertEquals(2, scheduleRecipes.size());

        scheduleRecipeDAO.updateScheduleRecipeStatusById(1);
        scheduleRecipes = scheduleRecipeDAO.queryIsNotDoneScheduleRecipesByDate(1);
        assertEquals(1, scheduleRecipes.size());
    }

    @Test
    public void queryScheduleRecipesIsNotDoneAndScheduleIdIsOneReturnListSizeShouldEqualOne(){
        Recipe recipe = new Recipe(0, "炒蛋", "炒蛋照片", 2, 0);
        Recipe recipe1 = new Recipe(0, "炒麵", "炒麵照片", 2, 0);
        recipeDAO.insertRecipe(recipe);
        recipeDAO.insertRecipe(recipe1);

        Schedule schedule = new Schedule(0, 0, 0);
        scheduleDAO.insertSchedule(schedule);
        ScheduleRecipe scheduleRecipe = new ScheduleRecipe(0, 1, 1, 1,1);
        ScheduleRecipe scheduleRecipe1 = new ScheduleRecipe(0, 2, 1, 1,0);
        long l = scheduleRecipeDAO.insertScheduleRecipe(scheduleRecipe);
        long l1 = scheduleRecipeDAO.insertScheduleRecipe(scheduleRecipe1);
        assertEquals(1, l);
        assertEquals(2, l1);
        List<ScheduleRecipe> scheduleRecipes = scheduleRecipeDAO.queryIsNotDoneScheduleRecipesByDate(1);
        assertEquals(1, scheduleRecipes.size());
        assertEquals(2, scheduleRecipes.get(0).rid.intValue());
    }

    @Test
    public void queryScheduleRecipesScheduleIdIsOne(){
        Recipe recipe = new Recipe(0, "炒蛋", "炒蛋照片", 2, 0);
        Recipe recipe1 = new Recipe(0, "炒麵", "炒麵照片", 2, 0);
        recipeDAO.insertRecipe(recipe);
        recipeDAO.insertRecipe(recipe1);

        Schedule schedule = new Schedule(0, 0, 0);
        scheduleDAO.insertSchedule(schedule);
        ScheduleRecipe scheduleRecipe = new ScheduleRecipe(0, 1, 1, 1,0);
        ScheduleRecipe scheduleRecipe1 = new ScheduleRecipe(0, 2, 1, 1,0);
        long l = scheduleRecipeDAO.insertScheduleRecipe(scheduleRecipe);
        long l1 = scheduleRecipeDAO.insertScheduleRecipe(scheduleRecipe1);
        assertEquals(1, l);
        assertEquals(2, l1);
        List<ScheduleRecipe> scheduleRecipes = scheduleRecipeDAO.queryScheduleRecipesByDate(1);
        assertEquals(2, scheduleRecipes.size());
        assertEquals(1, scheduleRecipes.get(0).sId.intValue());
        assertEquals(1, scheduleRecipes.get(1).sId.intValue());
    }

    @Test
    public void insertOneScheduleRecipeRecipeIdEqualsOne(){
        Recipe recipe = new Recipe(1, "炒蛋", "炒蛋照片", 2, 0);
        recipeDAO.insertRecipe(recipe);
        ScheduleRecipe scheduleRecipe = new ScheduleRecipe(0, 1, null, 1,0);
        long l = scheduleRecipeDAO.insertScheduleRecipe(scheduleRecipe);

        assertEquals(1, l);
    }
    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        FridgeDatabase.class
                ).allowMainThreadQueries()
                .build();
        scheduleRecipeDAO = database.scheduleRecipeDAO();
        recipeDAO = database.recipeDAO();
        scheduleDAO = database.scheduleDAO();
    }

    @After
    public void tearDown() throws Exception {
        database.close();
    }

}