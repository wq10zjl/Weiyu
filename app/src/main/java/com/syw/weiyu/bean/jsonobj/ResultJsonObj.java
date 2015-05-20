package com.syw.weiyu.bean.jsonobj;

import java.util.ArrayList;
import java.util.List;

/**
 * author: youwei
 * date: 2015-05-20
 * desc: 请求说说、评论列表的返回结果（用于JSON反序列化）
 */
public class ResultJsonObj {
    private int status;
    private int size;
    private int total;
    private List<PoiItemJsonObj> pois = new ArrayList<>();
    private String message;

    @Override
    public String toString() {
        return "{" +
                "status=" + status +
                ", size=" + size +
                ", total=" + total +
                ", pois=" + pois +
                ", message='" + message + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public List<PoiItemJsonObj> getPois() {
        return pois;
    }

    public void setPois(List<PoiItemJsonObj> pois) {
        this.pois = pois;
    }
}