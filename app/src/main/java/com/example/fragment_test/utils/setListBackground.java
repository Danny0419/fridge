package com.example.fragment_test.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.example.fragment_test.R;

public class setListBackground {
    //設定奇偶行數背景顏色
    public static void setListBackgroundColor(ViewGroup Container, Context context){
        Container.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < Container.getChildCount(); i++) {
                    View list = Container.getChildAt(i);
                    // 偶數行白底
                    if (i % 2 == 0) {
                        list.setBackgroundColor(ContextCompat.getColor(context, R.color.pink));
                    }
                    // 奇數行粉底
                    else {
                        list.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    }
                }
            }
        });
    }
}
