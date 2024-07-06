package com.example.fragment_test;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ConvertSize {
    /**
     * Covert dp to px
     * @param dp
     * @param context
     * @return pixel
     */
    public static float convertDpToPixel(float dp, Context context){
        float px = dp * getDensity(context);
        return px;
    }

    /**
     * Covert px to dp
     * @param px
     * @param context
     * @return dp
     */
    public static float convertPixelToDp(float px, Context context){
        float dp = px / getDensity(context);
        return dp;
    }

    /**
     * 取得螢幕密度
     * 120dpi = 0.75
     * 160dpi = 1 (default)
     * 240dpi = 1.5
     * @param context
     * @return
     */
    public static float getDensity(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }

    // LinearLayout設定大小與位置
    public static void setLinearSizeAndPosition(View view, int width, int height, int marginLeft, int marginTop, int marginRight, int marginEnd){
        // 設定寬、高
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);

        // 設定邊距
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginEnd);

        view.setLayoutParams(layoutParams);
    }

    // ConstraintLayout設定大小與位置
    public static void setConstraintSizeAndPosition(View view, int width, int height, int marginLeft, int marginTop, int marginRight, int marginEnd){
        // 設定寬、高
        ConstraintLayout.LayoutParams  layoutParams=new ConstraintLayout.LayoutParams(width,height);

        // 設定邊距
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginEnd);

        view.setLayoutParams(layoutParams);
    }

    // 實作轉換（px to dp）與設定大小與位置
    public static void adjustViewSizeAndPosition(View view, float width, float height, float marginLeft, float marginTop, float marginRight, float marginEnd) {
        Context context = view.getContext();

        // px轉dp
        width=convertPixelToDp(width,context);
        height=convertPixelToDp(height,context);
        marginLeft=convertPixelToDp(marginLeft,context);
        marginTop=convertPixelToDp(marginTop,context);
        marginRight=convertPixelToDp(marginRight,context);
        marginEnd=convertPixelToDp(marginEnd,context);

        // 判別被哪種layout所包裹
        if (view.getLayoutParams() instanceof LinearLayout.LayoutParams){
            setLinearSizeAndPosition(view, (int) width, (int) height, (int) marginLeft, (int) marginTop, (int) marginRight, (int) marginEnd);
        } else if (view.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {
            setConstraintSizeAndPosition(view, (int) width, (int) height, (int) marginLeft, (int) marginTop, (int) marginRight, (int) marginEnd);
        }
    }
}
