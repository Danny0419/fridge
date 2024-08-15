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
import com.example.fragment_test.entity.Day;
import com.example.fragment_test.entity.Recipe;

import java.util.ArrayList;
import java.util.Map;

public class ScheduleAdapterOld extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Day[] days = Day.values();
    private Map<String, ArrayList<Recipe>> recipes;

    class ViewHolder {
        TextView eachDayText;
        RecyclerView foodContainer;

    }

    public ScheduleAdapterOld(LayoutInflater layoutInflater, Map<String, ArrayList<Recipe>> recipes) {
        this.layoutInflater = layoutInflater;
        this.recipes = recipes;
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int position) {
        return days[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.recipe_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.eachDayText = convertView.findViewById(R.id.eachDayText);
            viewHolder.foodContainer = convertView.findViewById(R.id.foodContainer);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(parent.getContext(), 5);
            viewHolder.foodContainer.setLayoutManager(gridLayoutManager);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.eachDayText.setText(days[position].getDay());
        ArrayList<Recipe> recipe = recipes.get(days[position].getDay());
        if (recipe == null){
            recipe = new ArrayList<Recipe>();
        }
//        viewHolder.foodContainer.setAdapter(new ADayOfScheduleAdapter(recipe));

        /*點擊按鈕
        選擇要吃甚麼的區塊下拉與收合
        的動畫*/
        //下拉
        Button selectMealBtn = convertView.findViewById(R.id.select_meal_btn);
        RelativeLayout readyToCook = convertView.findViewById(R.id.ready_to_cook);
        TextView clickHint=convertView.findViewById(R.id.click_hint);
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
        View meal_today = convertView.findViewById(R.id.meal_today);
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
        return convertView;
    }
}
