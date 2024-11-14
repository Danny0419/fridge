package com.example.fragment_test.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.vo.RefrigeratorIngredientVO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RefrigeratorEachIngredientAdapter extends RecyclerView.Adapter<RefrigeratorEachIngredientAdapter.RefrigeratorKindViewHolder>{

    private List<RefrigeratorIngredientVO> kindOfIngredient;
    private RefrigeratorAdapter.OnClickListener onClickListener;
    private Context context;

    public RefrigeratorEachIngredientAdapter(Context context, List<RefrigeratorIngredientVO> kindOfIngredient) {
        this.kindOfIngredient = kindOfIngredient;
        this.context=context;
    }

    class RefrigeratorKindViewHolder extends RecyclerView.ViewHolder {

        ImageView ingredientImg;
        TextView ingredientName;
        TextView ingredientExpr;
        TextView ingredientQuan;
        TextView daysRemaining;


        public RefrigeratorKindViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImg = itemView.findViewById(R.id.ingredientImg);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            ingredientExpr = itemView.findViewById(R.id.ingredientExpiration);
            ingredientQuan = itemView.findViewById(R.id.ingredientQuan);
            daysRemaining = itemView.findViewById(R.id.ingredientDaysRemain);

        }
    }

    @NonNull
    @Override
    public RefrigeratorKindViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.refrigerator_card_item, parent, false);
        return new RefrigeratorKindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RefrigeratorKindViewHolder holder, int position) {
        RefrigeratorIngredientVO ingredient = kindOfIngredient.get(position);
        String imageName = ingredient.img; // 假设此方法返回 "broccoli"
        // 动态获取资源 ID
        int resId = holder.itemView.getContext().getResources().getIdentifier(
                imageName, "drawable", holder.itemView.getContext().getPackageName());

        if (resId != 0) { // 如果找到资源 ID
            holder.ingredientImg.setImageResource(resId);
        }
        holder.ingredientName.setText(ingredient.name);
//        holder.ingredientExpr.setText("保存期限："+ ingredient.earlyEx + "~" + ingredient.lastEx);
        holder.ingredientExpr.setText("有效日期："+ ingredient.getLastEx());
        holder.ingredientQuan.setText(Integer.toString(ingredient.sumQuantity) + " g");
        holder.itemView.setOnClickListener(view -> onClickListener.onClick(position, ingredient));
        int today = Integer.parseInt(DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()));
        holder.daysRemaining.setText("  " + (ingredient.earlyEx - today)+"天  ");

        //快過期提醒顏色
        if ((ingredient.earlyEx - today) <= 3) {
            //如果小於3天，則背景變紅色、文字變白色
            holder.daysRemaining.setBackgroundResource(R.drawable.warn_red_rectangle);
            holder.daysRemaining.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else if ((ingredient.earlyEx - today) <= 7) {
            //如果小於3天，則背景變黃色、文字變白色
            holder.daysRemaining.setBackgroundResource(R.drawable.warn_yellow_rectangle);
            holder.daysRemaining.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    public void setOnClickListener(RefrigeratorAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return kindOfIngredient.size();
    }


}
