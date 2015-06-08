package com.syw.weiyu.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

import java.io.Serializable;
import java.util.List;

/**
 * author: youwei
 * date: 2015-05-11
 * desc: 说说实体类
 */
public class Shuoshuo extends BmobObject {
    private long id;
    private MLocation location;
    private long timestamp;
    private String content;
    private String userName;
    private String userId;
//    private User user;
    private BmobGeoPoint gpsAdd;//用于Bmob存储位置点
    private String addressStr;//用于Bmob存储位置名
    private int commentCount;//评论数
//    private BmobRelation comments;
    private int likedCount;//喜欢数

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

    public BmobGeoPoint getGpsAdd() {
        return gpsAdd;
    }

    public void setGpsAdd(BmobGeoPoint gpsAdd) {
        this.gpsAdd = gpsAdd;
    }

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(String addressStr) {
        this.addressStr = addressStr;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(int likedCount) {
        this.likedCount = likedCount;
    }
}