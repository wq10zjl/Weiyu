package com.syw.weiyu.bean.jsonobj;

import java.util.ArrayList;
import java.util.List;

/**
 * author: youwei
 * date: 2015-05-20
 * desc: 请求说说列表的返回结果（用于JSON反序列化）
 */
public class NearbyShuoshuoListJsonObj {
    private int status;
    private int size;
    private int total;
    private List<NearbyShuoshuoItemJsonObj> contents = new ArrayList<>();

    @Override
    public String toString() {
        return "{" +
                "status=" + status +
                ", size=" + size +
                ", total=" + total +
                ", contents=" + contents +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<NearbyShuoshuoItemJsonObj> getContents() {
        return contents;
    }

    public void setContents(List<NearbyShuoshuoItemJsonObj> contents) {
        this.contents = contents;
    }
}