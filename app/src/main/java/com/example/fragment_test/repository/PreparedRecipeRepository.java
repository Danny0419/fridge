package com.example.fragment_test.repository;

import android.content.Context;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.PreparedRecipeDAO;
import com.example.fragment_test.entity.Ingredient;
import com.example.fragment_test.entity.PreparedRecipe;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeWithPreRecipeId;
import com.example.fragment_test.vo.RefrigeratorIngredientVO;
import com.example.fragment_test.entity.ShoppingIngredient;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class PreparedRecipeRepository {
    private static PreparedRecipeRepository preparedRecipeRepository;
    private final PreparedRecipeDAO preparedRecipeDAO;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RefrigeratorIngredientRepository refrigeratorIngredientRepository;
    private final ShoppingListIngredientRepository shoppingListIngredientRepository;

    private PreparedRecipeRepository(Context context) {
        this.preparedRecipeDAO = FridgeDatabase.getInstance(context).preparedRecipeDAO();
        this.recipeRepository = RecipeRepository.getInstance(context);
        this.recipeIngredientRepository = RecipeIngredientRepository.getInstance(context);
        this.refrigeratorIngredientRepository = RefrigeratorIngredientRepository.getInstance(context);
        this.shoppingListIngredientRepository = ShoppingListIngredientRepository.getInstance(context);
    }

    public static PreparedRecipeRepository getInstance(Context context) {
        if (preparedRecipeRepository == null) {
            preparedRecipeRepository = new PreparedRecipeRepository(context);
        }
        return preparedRecipeRepository;
    }

    public void addInterestingRecipe(Recipe recipe) {
        recipeRepository.storeRecipe(recipe);
        recipe.ingredients
                .forEach(recipeIngredient -> recipeIngredient.rId = recipe.id);
        recipeIngredientRepository.addRecipeIngredients(recipe.ingredients);
        PreparedRecipe preparedRecipe = new PreparedRecipe(0, recipe.id, recipe.src, null, 0);
        preparedRecipeDAO.insertPreparedRecipe(preparedRecipe);
        addNotSufficientIngredientsToShoppingList(recipe.ingredients.stream().map(o -> (Ingredient) o).toList());

    }

    public void addNotSufficientIngredientsToShoppingList(List<Ingredient> ingredients) {
        Map<String, RefrigeratorIngredientVO> refrigeratorIngredients = refrigeratorIngredientRepository.getRefrigeratorIngredientsSortedByName();
        for (Ingredient ingredient :
                ingredients) {
            Optional<RefrigeratorIngredientVO> refrigeratorIngredient = Optional.ofNullable(refrigeratorIngredients.get(ingredient.name));
            AtomicInteger difference = new AtomicInteger();
            refrigeratorIngredient.ifPresentOrElse(
                    r -> difference.set(ingredient.quantity - r.sumQuantity)
                    , () -> difference.set(ingredient.quantity)
            );
            if (difference.get() > 0) {
                shoppingListIngredientRepository.addShoppingItem(new ShoppingIngredient(
                        0,
                        ingredient.name,
                        ingredient.sort,
                        difference.get(),
                        0)
                );
            }
        }

    }

    public void schedule(RecipeWithPreRecipeId recipeWithPreRecipeId) {
        PreparedRecipe preparedRecipe = new PreparedRecipe(recipeWithPreRecipeId.pRId, recipeWithPreRecipeId.recipe.id, recipeWithPreRecipeId.recipe.src,1);
        preparedRecipeDAO.insertPreparedRecipe(preparedRecipe);
    }

    public List<RecipeWithPreRecipeId> getPreparedRecipes() {
        return preparedRecipeDAO.queryAllRecipes();
    }

    public void unSchedule(PreparedRecipe preparedRecipe) {
        preparedRecipeDAO.insertPreparedRecipe(preparedRecipe);
    }
}
