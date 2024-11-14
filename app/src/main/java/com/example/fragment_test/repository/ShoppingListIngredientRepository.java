package com.example.fragment_test.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.fragment_test.ServerAPI.RetrofitClient;
import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.ShoppingDAO;
import com.example.fragment_test.entity.Ingredient;
import com.example.fragment_test.entity.ShoppingIngredient;
import com.example.fragment_test.service.ShoppingService;
import com.example.fragment_test.vo.ShoppingItemVO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import io.reactivex.Single;

public class ShoppingListIngredientRepository {

    ShoppingDAO shoppingDAO;
    ShoppingService shoppingService;

    public static ShoppingListIngredientRepository shoppingListIngredientRepository;

    private ShoppingListIngredientRepository(Context context) {
        this.shoppingDAO = FridgeDatabase.getInstance(context).shoppingDAO();
        shoppingService = RetrofitClient.getRetrofitInstance().create(ShoppingService.class);
    }

    public Single<List<String>> getAllSortsOfIngredients() {
        return shoppingService.getAllSortsOfIngredients();
    }

    public static synchronized ShoppingListIngredientRepository getInstance(Context context) {
        if (shoppingListIngredientRepository == null) {
            shoppingListIngredientRepository = new ShoppingListIngredientRepository(context);
        }

        return shoppingListIngredientRepository;
    }

    public void addShoppingItem(ShoppingIngredient ingredient) {
        shoppingDAO.insertShoppingIngredient(ingredient);
    }

    public List<ShoppingItemVO> getShoppingList() {
        return shoppingDAO.getAllSumOfQuaGreaterThanZeroShoppingIngredientsGroupByName();
    }

    public void check(List<Ingredient> ingredients) {
        Map<String, ShoppingItemVO> shoppingItems = shoppingDAO.getAllSumOfQuaGreaterThanZeroShoppingIngredientsGroupByName()
                .stream()
                .collect(Collectors.toMap(ShoppingItemVO::getName, o -> o));
        for (Ingredient ingredient :
                ingredients) {
            Optional<ShoppingItemVO> shoppingItem = Optional.ofNullable(shoppingItems.get(ingredient.name));
            shoppingItem.ifPresent(item -> {
                int max = item.sumOfQuantity;
                int buyQuantity = ingredient.quantity;
                if (buyQuantity < max) {
                    shoppingDAO.insertShoppingIngredient(
                            new ShoppingIngredient(0,
                                    ingredient.name,
                                    item.sort,
                                    -buyQuantity,
                                    0
                            )
                    );
                } else {
                    shoppingDAO.insertShoppingIngredient(
                            new ShoppingIngredient(0,
                                    ingredient.name,
                                    item.sort,
                                    -max,
                                    0
                            )
                    );
                }
            });
        }

    }

    private @NonNull Map<String, Ingredient> computeIsUnfinished(Map<String, Ingredient> collect) {
        return collect.entrySet()
                .stream()
                .filter(entry -> entry.getValue().quantity > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Single<List<String>> getSortOfIngredientsName(String sort) {
        return shoppingService.getSortOfIngredientsName(sort);
    }

    public void deleteShoppingItem(ShoppingItemVO shoppingItemVO) {
        shoppingDAO.insertShoppingIngredient(new ShoppingIngredient(0,
                shoppingItemVO.name,
                shoppingItemVO.sort,
                -shoppingItemVO.sumOfQuantity,
                0)
        );
    }

    public void editShoppingItem(ShoppingItemVO shoppingItemVO, int editedQuantity) {
        int quantity = shoppingDAO.getShoppingItemQuantityByName(shoppingItemVO.name);
        shoppingDAO.insertShoppingIngredient(new ShoppingIngredient(0,
                shoppingItemVO.name,
                shoppingItemVO.sort,
                editedQuantity - quantity,
                0)
        );
    }
}
