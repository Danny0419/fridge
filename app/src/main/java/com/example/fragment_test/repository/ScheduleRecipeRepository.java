package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.ScheduleRecipeDAO;
import com.example.fragment_test.entity.Ingredient;
import com.example.fragment_test.entity.PreparedRecipe;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeIngredient;
import com.example.fragment_test.entity.RecipeWithPreRecipeId;
import com.example.fragment_test.entity.RecipeWithScheduledId;
import com.example.fragment_test.entity.RefrigeratorIngredient;
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
    private RefrigeratorIngredientRepository refrigeratorIngredientRepository;

    private ScheduleRecipeRepository(Context context) {
        this.scheduleRecipeDAO = FridgeDatabase.getInstance(context).scheduleRecipeDAO();
        this.preparedRecipeRepository = PreparedRecipeRepository.getInstance(context);
        this.refrigeratorIngredientRepository = RefrigeratorIngredientRepository.getInstance(context);
    }

    public static ScheduleRecipeRepository getInstance(Context context) {
        if (scheduleRecipeRepository == null) {
            scheduleRecipeRepository = new ScheduleRecipeRepository(context);
        }
        return scheduleRecipeRepository;
    }

    public void schedule(int date, int dayOfWeek, RecipeWithPreRecipeId recipeWithPreRecipeId) {
        ScheduleRecipe scheduleRecipe = new ScheduleRecipe(0, recipeWithPreRecipeId.id, date, dayOfWeek, 0);
        scheduleRecipeDAO.insertScheduleRecipe(scheduleRecipe);
        preparedRecipeRepository.schedule((recipeWithPreRecipeId));
    }

    public void unSchedule(int date, Recipe recipe) {
        scheduleRecipeDAO.deleteScheduleRecipeStatusByDateAndRecipeId(date, recipe.id);
        preparedRecipeRepository.unSchedule(new PreparedRecipe(0, recipe.id, 0));
    }

    public void cooking(RecipeWithScheduledId recipeWithScheduledId) {
        scheduleRecipeDAO.updateScheduleRecipeStatusById(recipeWithScheduledId.sRId);

        List<RefrigeratorIngredient> refrigeratorIngredients = refrigeratorIngredientRepository.getRefrigeratorIngredients();
        Map<String, List<RefrigeratorIngredient>> refrigeratorIngredientsSortedByName = refrigeratorIngredients
                .stream()
                .collect(Collectors.groupingBy(Ingredient::getName));

        List<RecipeIngredient> usedIngredients = recipeWithScheduledId.recipe.ingredients;
        for (int i = 0; i < usedIngredients.size(); i++) {
            RecipeIngredient usedIngredient = usedIngredients.get(i);
            List<RefrigeratorIngredient> ingredients = refrigeratorIngredientsSortedByName.get(usedIngredient.name);
            int usedIngredientQuantity = usedIngredient.quantity;
            for (int j = 0; j < ingredients.size(); j++) {
                RefrigeratorIngredient ingredient = ingredients.get(i);
                int ingredientQuantity = ingredient.quantity;

                if (ingredientQuantity < usedIngredientQuantity) {
                    usedIngredientQuantity -= ingredientQuantity;
                    ingredient.setQuantity(0);
                } else {
                    ingredient.setQuantity(ingredientQuantity - usedIngredientQuantity);
                    break;
                }
                refrigeratorIngredientRepository.useRefrigeratorIngredient(ingredient);
            }
        }
    }

    public boolean checkTodayIsDone(Integer sId) {
        List<ScheduleRecipe> scheduleRecipes = scheduleRecipeDAO.queryIsNotDoneScheduleRecipesByDate(sId);
        return scheduleRecipes.isEmpty();
    }

    public List<Recipe> getSpecificDayScheduleRecipes(int scheduleId) {
        return scheduleRecipeDAO.queryScheduleRecipesByDate(scheduleId);
    }

    public Map<DayOfWeek, List<RecipeWithScheduledId>> getAWeekSchedules() {
        String today = DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now());
        List<RecipeWithScheduledId> allNotFinishedSchedule = scheduleRecipeDAO.queryAllNotDoneAndUnexpiredScheduleRecipes(Integer.parseInt(today));
        return allNotFinishedSchedule.stream()
                .collect(Collectors.groupingBy(RecipeWithScheduledId::getDayOfWeek));
    }

    public List<Recipe> getSpecificDateScheduledRecipes(int date) {
        return scheduleRecipeDAO.queryScheduleRecipesByDate(date);
    }
}
