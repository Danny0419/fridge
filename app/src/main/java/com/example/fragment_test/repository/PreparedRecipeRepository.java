package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.PreparedRecipeDAO;
import com.example.fragment_test.entity.PreparedRecipe;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.Schedule;
import com.example.fragment_test.entity.ScheduleRecipe;

public class PreparedRecipeRepository {
    private static PreparedRecipeRepository preparedRecipeRepository;
    private final PreparedRecipeDAO preparedRecipeDAO;
    private final RecipeRepository recipeRepository;
    private final ScheduleRecipeRepository scheduleRecipeRepository;
    private PreparedRecipeRepository(Context context) {
        this.preparedRecipeDAO = FridgeDatabase.getInstance(context).preparedRecipeDAO();
        this.recipeRepository = RecipeRepository.getInstance(context);
        this.scheduleRecipeRepository =ScheduleRecipeRepository.getInstance(context);
    }

    public static PreparedRecipeRepository getInstance(Context context) {
        if (preparedRecipeRepository == null) {
            preparedRecipeRepository = new PreparedRecipeRepository(context);
        }
        return preparedRecipeRepository;
    }

    public void addInterestingRecipe(Recipe recipe) {
        recipeRepository.storeRecipe(recipe);
        PreparedRecipe preparedRecipe = new PreparedRecipe(0, recipe.id, null, 0);
        preparedRecipeDAO.insertPreparedRecipe(preparedRecipe);
    }

    public void schedule(Schedule schedule, PreparedRecipe preparedRecipe) {
        long scheduleRecipeId = scheduleRecipeRepository.schedule(new ScheduleRecipe(0, preparedRecipe.rId, schedule.date, schedule.dayOfWeek, 0));
        preparedRecipe.sRId = (int) scheduleRecipeId;
        preparedRecipe.scheduled = 1;
        preparedRecipeDAO.insertPreparedRecipe(preparedRecipe);
    }

}
