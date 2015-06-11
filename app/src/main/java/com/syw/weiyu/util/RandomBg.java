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
                R.drawable.s_2,
                R.drawable.s_3,
                R.drawable.s_4,
                R.drawable.s_5,
                R.drawable.s_6,
                R.drawable.s_7,
                R.drawable.s_8,
                R.drawable.s_9,
                R.drawable.s_10,
        };
        int i = Math.abs((int)n % bgs.length);//0~8
        return bgs[i];
    }
}
