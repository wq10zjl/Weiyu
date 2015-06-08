package com.syw.weiyu.bean;

/**
 * author: youwei
 * date: 2015-05-19
 * desc: 用户账户
 */
public class Account {
    private String id;
    private String name;
    private String gender;
    private String token;
    private String bmobObjectId;//用户账户有很多数据操作需要这个id才行

    public Account() {}
    public Account(String id, String name, String gender, String token) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBmobObjectId() {
        return bmobObjectId;
    }

    public void setBmobObjectId(String bmobObjectId) {
        this.bmobObjectId = bmobObjectId;
    }
}
