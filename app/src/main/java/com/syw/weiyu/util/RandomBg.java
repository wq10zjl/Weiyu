package com.syw.weiyu.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import com.syw.weiyu.R;

/**
 * author: youwei
 * date: 2015-06-11
 * desc: 随机背景
 */
public class RandomBg {
//    public static int getBgResId(long n) {
//        int[] bgs = new int[]{
//                R.color.white,
//                R.color.white,
//                R.color.white,
//                R.color.white,
//                R.color.white,
//                R.color.white,
//                R.color.white,

//                R.drawable.s_2,
//                R.drawable.s_3,
//                R.drawable.s_4,
//                R.drawable.s_5,
//                R.drawable.s_6,
//                R.drawable.s_7,
//                R.drawable.s_8,
//                R.drawable.s_9,
//                R.drawable.s_10,

//                R.drawable.l_2,
//                R.drawable.l_3,
//                R.drawable.l_4,
//                R.drawable.l_5,
//                R.drawable.l_6,
//                R.drawable.l_7,
//                R.drawable.l_8,
//                R.drawable.l_9,
//                R.drawable.l_10,
//                R.drawable.l_11,
//                R.drawable.l_12,
//                R.drawable.l_13,
//                R.drawable.l_14,
//                R.drawable.l_15,
//                R.drawable.l_16,
//                R.drawable.l_17,
//                R.drawable.l_18,
//                R.drawable.l_19,
//                R.drawable.l_20,
//                R.drawable.l_21,
//                R.drawable.l_22,
//                R.drawable.l_23,
//                R.drawable.l_24,
//                R.drawable.l_25,
//                R.drawable.l_26,
//                R.drawable.l_27,
//                R.drawable.l_28,
//                R.drawable.l_29,
//                R.drawable.l_30,
//
//        };
//        int i = Math.abs((int)n % bgs.length);
//        return bgs[i];
//    }


    public static boolean isWhite(int resId) {
        return resId==R.color.white;
    }

    public static Drawable getBgColorDrawable(long n) {
        int i = Math.abs((int)n % colors.length);
        return drawables[i];
    }

    static int[] colors;
    static Drawable[] drawables;
    static {
        colors = new int[]{
                Color.parseColor("#000000"),
                Color.parseColor("#87CEEB"),
                Color.parseColor("#8FBC8F"),
                Color.parseColor("#EEDD82"),
                Color.parseColor("#BC8F8F"),
                Color.parseColor("#CD5C5C"),
                Color.parseColor("#A0522D"),
                Color.parseColor("#FA8072"),
                Color.parseColor("#FF69B4"),
                Color.parseColor("#FFC0CB"),
                Color.parseColor("#8DB6CD"),
                Color.parseColor("#008B8B"),
                Color.parseColor("#8B5742"),
                Color.parseColor("#FF7256"),
                Color.parseColor("#FF83FA"),
                Color.parseColor("#FF7F24"),
                Color.parseColor("#CDAF95"),
                Color.parseColor("#8B0000"),
        };
        drawables = new Drawable[colors.length];
        for (int i = 0; i < colors.length; i++) {
            drawables[i] = new ColorDrawable(colors[i]);
        }
    }

}
