package com.example.fragment_test.database;

import androidx.room.ExperimentalRoomApi;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.Schedule;
import com.example.fragment_test.entity.Step;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@ExperimentalRoomApi
@RunWith(AndroidJUnit4.class)
@SmallTest
public class StepDAOTest extends TestCase {

    public FridgeDatabase database;
    public StepDAO stepDAO;
    public RecipeDAO recipeDAO;
    public ScheduleDAO scheduleDAO;

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        FridgeDatabase.class
                ).allowMainThreadQueries()
                .build();
        stepDAO = database.stepDAO();
        recipeDAO = database.recipeDAO();
        scheduleDAO = database.scheduleDAO();
    }

    @After
    public void close() {
        database.close();
    }

    @Test
    public void insertFourStepsWithRecipeIdOneQueryListSizeShouldEqualFour(){
        List<Schedule> schedules = List.of(
                new Schedule(0, 5, "2024-7-25", 0)
        );
        scheduleDAO.insertSchedule(schedules.get(0));

        List<Recipe> recipes = List.of(
                new Recipe(0, "荷包蛋", "荷包蛋照片", 1, 0, 0, 1),
                new Recipe(0, "荷包蛋", "荷包蛋照片", 2, 0 ,0, 1),
                new Recipe(0, "荷包蛋", "荷包蛋照片", 3, 0,0, 1)
        );
        recipes.forEach((recipe) -> recipeDAO.insertRecipe(recipe));

        List<Step> steps = List.of(
                new Step(1, "第一步驟"),
                new Step(1, "第二步驟"),
                new Step(1, "第三步驟"),
                new Step(1, "第四步驟")
        );
        steps.forEach((step -> stepDAO.insertStep(step)));
        List<Step> stepsByRid = stepDAO.getStepsByRid(1);
        assertEquals(4, stepsByRid.size());
    }

}