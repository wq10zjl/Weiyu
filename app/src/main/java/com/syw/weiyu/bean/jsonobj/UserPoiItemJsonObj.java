package com.syw.weiyu.bean.jsonobj;

/**
 * author: youwei
 * date: 2015-05-29
 * desc: 用户POI数据项，用于json反序列化
 */
public class UserPoiItemJsonObj {
    private String userId;
    private String name;
    private String gender;
    private String province;
    private String city;
    private String district;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
