package com.syw.weiyu.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

import java.io.Serializable;

/**
 * Created by songyouwei on 2015/1/30.
 * 用户实体类
 * 用户对象包含的location为自己的MLocation对象
 */
public class User extends BmobObject implements Serializable {
    private String id;
    private String name;
    private String gender;
    private String tags;
    private String organization;
    private MLocation location;
    private long lastOnlineTimestamp;//最后更新时间
    private String bmobObjectId;//在bmob中操作数据经常使用这个唯一id，但使用finalDB不能保存它，这里作另外保存
    private BmobGeoPoint gpsAdd;//用于Bmob存储位置点
    private String addressStr;//用于Bmob存储位置名

    public User() {}
    public User(String id, String name, String gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", tags='" + tags + '\'' +
                ", organization='" + organization + '\'' +
                ", location=" + location +
                '}';
    }

    public BmobGeoPoint getGpsAdd() {
        return gpsAdd;
    }
    public void setGpsAdd(BmobGeoPoint gpsAdd) {
        this.gpsAdd = gpsAdd;
    }

    public MLocation getLocation() {
        return location;
    }

    public void setLocation(MLocation location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(String addressStr) {
        this.addressStr = addressStr;
    }

    public long getLastOnlineTimestamp() {
        return lastOnlineTimestamp;
    }

    public void setLastOnlineTimestamp(long lastOnlineTimestamp) {
        this.lastOnlineTimestamp = lastOnlineTimestamp;
    }

    public String getBmobObjectId() {
        return bmobObjectId;
    }

    public void setBmobObjectId(String bmobObjectId) {
        this.bmobObjectId = bmobObjectId;
    }
}
