package com.example.fragment_test.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.entity.RecipeWithScheduledId;
import com.example.fragment_test.ui.schedule.MealsPrepareDialogActivity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ScheduleAdapter extends BaseAdapter {
    private LocalDate[] dates;
    private Map<DayOfWeek, List<RecipeWithScheduledId>> scheduleRecipes;
    private ScheduleEachRecipeAdapter.OnClickListener onClickListener;

    class ViewHolder {
        TextView eachDayText;
        RecyclerView eachRecipeRecyclerView;
    }

    public ScheduleAdapter(LocalDate[] dates, Map<DayOfWeek, List<RecipeWithScheduledId>> scheduleRecipes) {
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
        Optional<List<RecipeWithScheduledId>> dayScheduleRecipesOpt = Optional.ofNullable(scheduleRecipes.get(dates[position].getDayOfWeek()));
        List<RecipeWithScheduledId> dayScheduleRecipes = dayScheduleRecipesOpt.orElse(new ArrayList<>());

        String format = DateTimeFormatter.BASIC_ISO_DATE.format(dates[position]);
        ScheduleEachRecipeAdapter scheduleEachRecipeAdapter = new ScheduleEachRecipeAdapter(dayScheduleRecipes, Integer.parseInt(format));
        scheduleEachRecipeAdapter.setOnClickListener(onClickListener);
        viewHolder.eachRecipeRecyclerView.setAdapter(scheduleEachRecipeAdapter);

        Button selectMealBtn = view.findViewById(R.id.select_meal_btn);
        selectMealBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(v.getContext(), MealsPrepareDialogActivity.class);
            intent.putExtra("date", Integer.parseInt(format));
            intent.putExtra("dayOfWeek", dates[position].getDayOfWeek().toString());
            v.getContext().startActivity(intent);
        });
        return view;
    }

    public void setOnClickListener(ScheduleEachRecipeAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
