package com.example.fragment_test.repository;


import android.content.Context;

import androidx.annotation.NonNull;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.RefrigeratorIngredientDAO;
import com.example.fragment_test.entity.Ingredient;
import com.example.fragment_test.entity.RecipeIngredient;
import com.example.fragment_test.entity.RefrigeratorIngredient;
import com.example.fragment_test.entity.RefrigeratorIngredientDetailVO;
import com.example.fragment_test.entity.RefrigeratorIngredientVO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RefrigeratorIngredientRepository {
    private final RefrigeratorIngredientDAO refrigeratorIngredientDAO;
    private final ShoppingListIngredientRepository shoppingListIngredientRepository;
    private static RefrigeratorIngredientRepository refrigeratorIngredientRepository;

    private RefrigeratorIngredientRepository(Context context) {
        // 确保使用 refrigeratorIngredientDAO 方法
        this.refrigeratorIngredientDAO = FridgeDatabase.getInstance(context).refrigeratorIngredientDAO();
        this.shoppingListIngredientRepository = ShoppingListIngredientRepository.getInstance(context);
    }

    public static RefrigeratorIngredientRepository getInstance(Context context) {
        if (refrigeratorIngredientRepository == null) {
            refrigeratorIngredientRepository = new RefrigeratorIngredientRepository(context);
        }
        return refrigeratorIngredientRepository;
    }

    public void buyIngredients(List<RefrigeratorIngredient> ingredients) {

        ingredients.forEach(refrigeratorIngredientDAO::insertIngredient);

        List<Ingredient> collect = ingredients.stream().
                map(o -> (Ingredient) o)
                .collect(Collectors.toList());

        shoppingListIngredientRepository.check(collect);
    }

    private String fetchGroupKey(RefrigeratorIngredient ingredient){
        return ingredient.name +"#"+ ingredient.expiration;
    }

    private @NonNull Map<String, Ingredient> computeIsUnfinished(Map<String, Ingredient> collect) {
        return collect.entrySet()
                .stream()
                .filter(entry -> entry.getValue().quantity > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private @NonNull Map<String, Ingredient> computeIsFinished(Map<String, Ingredient> collect) {
        return collect.entrySet()
                .stream()
                .filter(entry -> entry.getValue().quantity <= 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static @NonNull Map<String, Ingredient> computeSameIngredients(List<Ingredient> collapse) {
        return collapse.stream()
                .collect(Collectors.toMap(Ingredient::getName, a -> a,
                        (o1, o2) -> o1.setQuantity(o1.quantity + o2.quantity)));
    }

    public Map<String, List<RefrigeratorIngredientVO>> getRefrigeratorIngredients() {
        String now = DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now());
        List<RefrigeratorIngredientVO> list = refrigeratorIngredientDAO.getQuantityGreaterZeroAndNotExpiredIngredientsOverallInfo(Integer.parseInt(now));
        return sortIngredients(list);
    }

//    public Map<String, List<RefrigeratorIngredient>> getAllIngredients() {
//        List<RefrigeratorIngredient> list = refrigeratorIngredientDAO.getAllRefrigeratorIngredients();
//        return sortIngredients(list);
//    }

    private Map<String, List<RefrigeratorIngredientVO>> sortIngredients(List<RefrigeratorIngredientVO> list) {
        return list.stream()
                .collect(Collectors.groupingBy(RefrigeratorIngredientVO::getSort));
    }

    public void consumeIngredients(List<RecipeIngredient> recipesUsingIngredients) {
        recipesUsingIngredients.forEach(recipeIngredient -> {
            refrigeratorIngredientDAO.insertIngredient(new RefrigeratorIngredient(0, recipeIngredient.name, -recipeIngredient.quantity, null, null, null, null));
        });
    }

    public void takeOutIngredients(RefrigeratorIngredient ingredient) {
        refrigeratorIngredientDAO.insertIngredient(ingredient);
    }

    public List<RefrigeratorIngredientDetailVO> searchRefrigeratorIngredientDetail(RefrigeratorIngredientVO refrigeratorIngredientVO) {
        LocalDate now = LocalDate.now();
        String format = DateTimeFormatter.BASIC_ISO_DATE.format(now);
        int today = Integer.parseInt(format);
        return refrigeratorIngredientDAO.getIngredientByName(refrigeratorIngredientVO.name, today);
    }
}
