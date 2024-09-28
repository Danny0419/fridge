package com.example.fragment_test.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.ScheduleRecipe;
import com.example.fragment_test.ui.schedule.MealsPrepareDialogActivity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScheduleAdapter extends BaseAdapter {
    private LocalDate[] dates;
    private Map<DayOfWeek, List<ScheduleRecipe>> scheduleRecipes;
    class ViewHolder {
        TextView eachDayText;
        RecyclerView eachRecipeRecyclerView;
    }

    public ScheduleAdapter(LocalDate[] dates, Map<DayOfWeek, List<ScheduleRecipe>> scheduleRecipes) {
        this.dates = dates;
        this.scheduleRecipes = scheduleRecipes;
    }

    @Override
    public int getCount() {
        return dates.length;
    }

    @Override
    public Object getItem(int i) {
        return dates[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.eachDayText = view.findViewById(R.id.eachDayText);
            viewHolder.eachRecipeRecyclerView = view.findViewById(R.id.food_item);

            LinearLayoutManager layoutManager = new LinearLayoutManager(viewGroup.getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            viewHolder.eachRecipeRecyclerView.setLayoutManager(layoutManager);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.eachDayText.setText(dates[position].getDayOfWeek().toString());
        Optional<List<ScheduleRecipe>> dayScheduleRecipesOpt = Optional.ofNullable(scheduleRecipes.get(dates[position].getDayOfWeek()));
        List<ScheduleRecipe> dayScheduleRecipes = dayScheduleRecipesOpt.orElse(new ArrayList<>());
        Stream<Recipe> recipeStream = dayScheduleRecipes.stream().map(ScheduleRecipe::getRecipe);
        List<Recipe> recipes = recipeStream.collect(Collectors.toList());
        viewHolder.eachRecipeRecyclerView.setAdapter(new ScheduleEachRecipeAdapter(recipes));

        /*點擊按鈕
        選擇要吃甚麼的區塊下拉與收合
        的動畫*/
        //下拉
        Button selectMealBtn = view.findViewById(R.id.select_meal_btn);
        RelativeLayout readyToCook = view.findViewById(R.id.ready_to_cook);
        TextView clickHint=view.findViewById(R.id.click_hint);
        selectMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyToCook.getVisibility() == View.GONE) {
                    ObjectAnimator slideDown = ObjectAnimator.ofFloat(readyToCook, "translationY", -readyToCook.getHeight(), 0f);
                    slideDown.setDuration(500);
                    slideDown.start();
                    readyToCook.setVisibility(View.VISIBLE);
                    clickHint.setVisibility(View.VISIBLE);
                    selectMealBtn.setVisibility(View.GONE);
                }
            }
        });

        //收合/彈跳預備食譜
        View meal_today = view.findViewById(R.id.meal_today);
        meal_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收合
                if (readyToCook.getVisibility() == View.VISIBLE) {
                    ObjectAnimator slideUp = ObjectAnimator.ofFloat(readyToCook, "translationY", 0f, -readyToCook.getHeight());
                    slideUp.setDuration(500);
                    slideUp.start();
                    slideUp.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            readyToCook.setVisibility(View.GONE);
                            clickHint.setVisibility(View.GONE);
                            selectMealBtn.setVisibility(View.VISIBLE);
                        }
                    });
                }
                //彈跳預備食譜
                else {
                    Intent intent = new Intent(v.getContext(), MealsPrepareDialogActivity.class);
                    v.getContext().startActivity(intent);
                }
            }
        });
        return view;
    }
}
