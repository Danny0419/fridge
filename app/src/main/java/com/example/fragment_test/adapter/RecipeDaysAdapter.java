package com.example.fragment_test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.pojo.Day;
import com.example.fragment_test.pojo.Recipe;

import java.util.ArrayList;
import java.util.Map;

public class RecipeDaysAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Day[] days;
    private Map<String, ArrayList<Recipe>> recipes;

    class ViewHolder {
        TextView eachDayText;
        RecyclerView foodContainer;

    }

    public RecipeDaysAdapter() {
    }

    public RecipeDaysAdapter(LayoutInflater layoutInflater, Map<String, ArrayList<Recipe>> recipes) {
        this.layoutInflater = layoutInflater;
        this.days = Day.values();
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
//            gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(convertView.getContext());
//            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
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
        viewHolder.foodContainer.setAdapter(new RecipeAdapter(recipe));
        return convertView;
    }
}
