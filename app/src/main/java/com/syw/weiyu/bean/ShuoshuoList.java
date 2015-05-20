package com.syw.weiyu.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * author: songyouwei
 * date: 2015-05-19
 * desc: 说说列表
 */
public class ShuoshuoList {

    private List<Shuoshuo> shuoshuoList = new ArrayList<>();

    public List<Shuoshuo> get() {
        return shuoshuoList;
    }
    public void set(List<Shuoshuo> list) {
        shuoshuoList.clear();
        shuoshuoList.addAll(list);
    }
    public void append(List<Shuoshuo> list) {
        shuoshuoList.addAll(list);
    }

    public Shuoshuo getItem(int position) {
        return shuoshuoList.get(position);
    }
}
