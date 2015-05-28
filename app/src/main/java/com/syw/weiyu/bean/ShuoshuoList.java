package com.syw.weiyu.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * author: songyouwei
 * date: 2015-05-28
 * desc:
 */
public class ShuoshuoList {
    private int total;
    private List<Shuoshuo> shuoshuos = new ArrayList<>();

    public ShuoshuoList(int total, List<Shuoshuo> shuoshuos) {
        this.total = total;
        this.shuoshuos = shuoshuos;
    }

    public List<Shuoshuo> getShuoshuos() {
        return shuoshuos;
    }

    public void setShuoshuos(List<Shuoshuo> shuoshuos) {
        this.shuoshuos = shuoshuos;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
