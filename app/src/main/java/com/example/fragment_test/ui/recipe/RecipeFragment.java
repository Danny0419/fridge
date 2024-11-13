package com.example.fragment_test.ui.recipe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.RecipeRecommend.RecipeRecommendation;
import com.example.fragment_test.adapter.RecipeAdapter;
import com.example.fragment_test.databinding.FragmentRecipeBinding;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecipeFragment extends Fragment {

    private FragmentRecipeBinding binding;
    private RecipeViewModel mViewModel;
    private RecyclerView recipeRecyclerView;
    private RecipeRecommendation recipeRecommendation;
    private boolean isCollectionSelected = false;

    public static RecipeFragment newInstance() {
        return new RecipeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(RecipeViewModel.class);
//        mViewModel.loadRecommendRecipes();
        recipeRecommendation = new RecipeRecommendation();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentRecipeBinding.inflate(inflater);
        recipeRecyclerView = binding.recipeRecyclerView;

        FragmentActivity fragmentActivity = requireActivity();
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main2);
        fragmentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.recipe_toolbar, menu);
            }

            //toolbar點擊
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main2);
                //規劃
                if (itemId == R.id.schedule) {
                    //在跳轉之前先將原頁面退回到首頁，防止navbar一直導到跳轉頁面
                    navController.popBackStack(R.id.navigation_home, false);
                    //頁面跳轉
                    navController.navigate(R.id.navigation_schedule);
                }
                //收藏
                else if (itemId == R.id.collection) {
                    mViewModel.showCollectionRecipe();
                    //如果已經點選過，變回為填滿
                    if (isCollectionSelected) {
                        menuItem.setIcon(R.drawable.bookmark);
                    }
                    //若未被點選過，變成填滿
                    else {
                        menuItem.setIcon(R.drawable.bookmark__filled);
                    }
                    isCollectionSelected = !isCollectionSelected; // 更新狀態
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.STARTED);

        recipeRecommendation.init(getContext(), new RecipeRecommendation.InitCallback() {
            @Override
            public void onSuccess(Map<String, RecipeRecommendation.FridgeIngredient> fridgeIngredients) {
                getRecommendationsAndUpdateUI(fridgeIngredients, navController);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

//        mViewModel.getRecipes()
//                .observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
//                    @Override
//                    public void onChanged(List<Recipe> recipes) {
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                        recipeRecyclerView.setLayoutManager(linearLayoutManager);
//                        RecipeAdapter recipeAdapter = new RecipeAdapter(getContext(), recipes);
//                        recipeAdapter.setListener((position, recipe) -> {
//                            Bundle bundle = new Bundle();
//                            bundle.putParcelable("recipe", recipe);
//                            try {
//                                navController.navigate(R.id.navigation_recipe_detail, bundle);
//                            }catch (RuntimeException e) {
//                                Toast.makeText(getContext(), "請在嘗試一次", Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        });
//                        recipeRecyclerView.setAdapter(recipeAdapter);
//                    }
//                });
        return binding.getRoot();
    }

    private void getRecommendationsAndUpdateUI(Map<String, RecipeRecommendation.FridgeIngredient> fridgeIngredients, NavController navController) {
        List<RecipeRecommendation.Recommendation> recommendations = recipeRecommendation.getRecommendations(fridgeIngredients);
        if (recommendations != null && !recommendations.isEmpty()) {
            List<Recipe> recipes = recommendations.stream()
                    .map(o -> {
                        com.example.fragment_test.ServerAPI.Recipe recipe = o.getRecipe();
                        Recipe r = new Recipe(recipe.getRecipe_id()
                                , recipe.getRecipe_name()
                                , recipe.getPicture()
                                , recipe.getServings()
                        );
                        List<RecipeIngredient> ingredients = new ArrayList<>();
                        recipe.getIngredients()
                                .forEach(o1 -> {
                                    String ingredientNeed = o1.getIngredient_need();
                                    String quantity = ingredientNeed.substring(0, ingredientNeed.length() - 1);
                                    ingredients.add(new RecipeIngredient(o1.getIngredient_name(), Integer.parseInt(quantity)));
                                });
                        r.setIngredients(ingredients);
                        return r;
                    })
                    .collect(Collectors.toList());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recipeRecyclerView.setLayoutManager(linearLayoutManager);
            RecipeAdapter recipeAdapter = new RecipeAdapter(getContext(), recipes);
                        recipeAdapter.setListener((position, recipe) -> {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("recipe", recipe);
                            try {
                                navController.navigate(R.id.navigation_recipe_detail, bundle);
                            }catch (RuntimeException e) {
                                Toast.makeText(getContext(), "請在嘗試一次", Toast.LENGTH_SHORT).show();
                            }


                        });
                        recipeRecyclerView.setAdapter(recipeAdapter);
        } else {
            Log.e("RecommendationActivity", "No recommendations found.");
        }
    }
}