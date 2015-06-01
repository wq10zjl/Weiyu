package com.syw.weiyu.bean.jsonobj;

/**
 * author: youwei
 * date: 2015-05-29
 * desc:
 */
public class SinglePoiJsonObj {
    private int status;
    private UserItemJsonObj poi;
    private String message;

    @Override
    public String toString() {
        return "{" +
                "status=" + status +
                ", poi=" + poi +
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

    public UserItemJsonObj getPoi() {
        return poi;
    }

    public void setPoi(UserItemJsonObj poi) {
        this.poi = poi;
    }
}
