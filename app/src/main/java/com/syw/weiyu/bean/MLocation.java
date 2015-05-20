package com.syw.weiyu.bean;

import com.baidu.location.BDLocation;
import com.syw.weiyu.AppConstants;

import java.io.Serializable;

/**
 * Created by songyouwei on 2015/2/7.
 * 自己的位置信息类
 * 对BDLocation进行持久化时无法操作“省市区”的内容，因而转而使用这个MLocation
 * 可由BDLocation对象构造
 */
public class MLocation implements Serializable {
    private String id;
    private String address;
    private String city;
    private String province;
    private String district;
    private String longitude;//经度
    private String latitude;//纬度

    @Override
    public String toString() {
        return "MLocation{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", district='" + district + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }

    public MLocation(BDLocation bdLocation) {
        if (bdLocation == null) {
            //定位失效时使用默认位置
            this.address = AppConstants.address_default;
            this.city = AppConstants.city_default;
            this.province = AppConstants.province_default;
            this.longitude = AppConstants.longitude_default;
            this.latitude = AppConstants.latitude_default;
        } else {
            this.address = bdLocation.getAddrStr();
            this.city = bdLocation.getCity();
            this.province = bdLocation.getProvince();
            this.district = bdLocation.getDistrict();
            this.longitude = String.valueOf(bdLocation.getLongitude());
            this.latitude = String.valueOf(bdLocation.getLatitude());
        }
    }

    public MLocation(String city, String province, String district) {
        this.city = city;
        this.province = province;
        this.district = district;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
