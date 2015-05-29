package com.syw.weiyu.bean.jsonobj;

/**
 * author: youwei
 * date: 2015-05-29
 * desc:
 */
public class SingleResultJsonObj {
    private int status;
    private UserPoiItemJsonObj poi;
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

    public UserPoiItemJsonObj getPoi() {
        return poi;
    }

    public void setPoi(UserPoiItemJsonObj poi) {
        this.poi = poi;
    }
}
