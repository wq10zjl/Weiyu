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

    public List<Shuoshuo> getShuoshuoList() {
        return shuoshuoList;
    }
    public void setShuoshuoList(List<Shuoshuo> shuoshuoList) {
        this.shuoshuoList = shuoshuoList;
    }

    public Shuoshuo getShuoshuoItem(int position) {
        return shuoshuoList.get(position);
    }
}
