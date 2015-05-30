package com.syw.weiyu.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * author: youwei
 * date: 2015-05-11
 * desc: 说说实体类
 */
public class Shuoshuo implements Serializable {
    private long id;
    private MLocation location;
    private long timestamp;
    private String content;
    private String userName;
    private String userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MLocation getLocation() {
        return location;
    }

    public void setLocation(MLocation location) {
        this.location = location;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}