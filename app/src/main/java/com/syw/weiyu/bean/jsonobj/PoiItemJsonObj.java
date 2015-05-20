package com.syw.weiyu.bean.jsonobj;

/**
 * author: youwei
 * date: 2015-05-20
 * desc: 说说、评论、poi数据项（用于JSON反序列化）
 */
public class PoiItemJsonObj {
    private long id;
    private String province;
    private String city;
    private String district;
    private long timestamp;
    private String content;
    private String userName;
    private String userId;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", timestamp=" + timestamp +
                ", content='" + content + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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