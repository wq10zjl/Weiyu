package com.syw.weiyu.util;

import com.syw.weiyu.R;

/**
 * author: youwei
 * date: 2015-06-11
 * desc: 随机背景
 */
public class RandomBg {
    public static int getBgResId(long n) {
        int[] bgs = new int[]{
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

                R.drawable.l_2,
                R.drawable.l_3,
                R.drawable.l_4,
                R.drawable.l_5,
                R.drawable.l_6,
                R.drawable.l_7,
                R.drawable.l_8,
                R.drawable.l_9,
                R.drawable.l_10,
                R.drawable.l_11,
                R.drawable.l_12,
                R.drawable.l_13,
                R.drawable.l_14,
                R.drawable.l_15,
                R.drawable.l_16,
                R.drawable.l_17,
                R.drawable.l_18,
                R.drawable.l_19,
                R.drawable.l_20,
                R.drawable.l_21,
                R.drawable.l_22,
                R.drawable.l_23,
                R.drawable.l_24,
                R.drawable.l_25,
                R.drawable.l_26,
                R.drawable.l_27,
                R.drawable.l_28,
                R.drawable.l_29,
                R.drawable.l_30,
        };
        int i = Math.abs((int)n % bgs.length);//0~8
        return bgs[i];
    }

    public static boolean isWhite(int resId) {
        return resId==R.color.white;
    }
}
