package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.entity.Ingredient;
import com.example.fragment_test.entity.RecipeIngredient;
import com.example.fragment_test.entity.RecipeWithScheduledId;
import com.example.fragment_test.entity.RefrigeratorIngredient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CookingRepository {
    private static CookingRepository cookingRepository;
    private final RefrigeratorIngredientRepository refrigeratorIngredientRepository;
    private final ScheduleRecipeRepository scheduleRecipeRepository;
    private CookingRepository(Context context) {
        this.refrigeratorIngredientRepository = RefrigeratorIngredientRepository.getInstance(context);
        this.scheduleRecipeRepository = ScheduleRecipeRepository.getInstance(context);
    }

    public static CookingRepository getInstance(Context context) {
        if (cookingRepository == null) {
            cookingRepository = new CookingRepository(context);
        }
        return cookingRepository;
    }

    public Boolean checkAreIngredientsSufficient(RecipeWithScheduledId recipeWithScheduledId) {
        List<RecipeIngredient> ingredients = recipeWithScheduledId.recipe.ingredients;
        return refrigeratorIngredientRepository.checkAreIngredientsSufficient(ingredients);
    }

    public void cooking(RecipeWithScheduledId recipeWithScheduledId) {
        scheduleRecipeRepository.completeScheduleRecipe(recipeWithScheduledId);

        List<RefrigeratorIngredient> refrigeratorIngredients = refrigeratorIngredientRepository.getRefrigeratorIngredients();
        Map<String, List<RefrigeratorIngredient>> refrigeratorIngredientsSortedByName = refrigeratorIngredients
                .stream()
                .collect(Collectors.groupingBy(Ingredient::getName));

        List<RecipeIngredient> usedIngredients = recipeWithScheduledId.recipe.ingredients;
        for (int i = 0; i < usedIngredients.size(); i++) {
            RecipeIngredient usedIngredient = usedIngredients.get(i);
            List<RefrigeratorIngredient> ingredients = refrigeratorIngredientsSortedByName.get(usedIngredient.name);
            int usedIngredientQuantity = usedIngredient.quantity;
            for (int j = 0; usedIngredientQuantity > 0 && j < ingredients.size(); j++) {
                RefrigeratorIngredient ingredient = ingredients.get(i);
                int ingredientQuantity = ingredient.quantity;

                int diff = ingredientQuantity - usedIngredientQuantity;
                usedIngredientQuantity = -diff;
                ingredient.setQuantity(Math.max(diff, 0));
                refrigeratorIngredientRepository.useRefrigeratorIngredient(ingredient);
            }
        }
    }
}
