package com.example.fragment_test.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.entity.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduleAdapter extends BaseAdapter {
    private String[] dayOfWeek;
    private Map<Integer, List<Recipe>> scheduleRecipes;
    private LayoutInflater layoutInflater;
    class ViewHolder {
        TextView eachDayText;
        RecyclerView foodContainer;
    }

    public ScheduleAdapter(String[] dayOfWeek, Map<Integer, List<Recipe>> scheduleRecipes, LayoutInflater layoutInflater) {
        this.dayOfWeek = dayOfWeek;
        this.scheduleRecipes = scheduleRecipes;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return dayOfWeek.length;
    }

    @Override
    public Object getItem(int i) {
        return dayOfWeek[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.schedule_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.eachDayText = view.findViewById(R.id.eachDayText);
            viewHolder.foodContainer = view.findViewById(R.id.food_item);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(viewGroup.getContext(), 5);
            viewHolder.foodContainer.setLayoutManager(gridLayoutManager);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.eachDayText.setText(dayOfWeek[position]);
        List<Recipe> recipe = scheduleRecipes.get(position);
        if (recipe == null){
            recipe = new ArrayList<>();
        }
//        viewHolder.foodContainer.setAdapter(new ADayOfScheduleAdapter(recipe));

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

        //收合
        View meal_today = view.findViewById(R.id.meal_today);
        meal_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        return view;
    }
}
