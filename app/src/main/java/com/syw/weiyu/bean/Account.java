package com.syw.weiyu.bean;

/**
 * author: youwei
 * date: 2015-05-19
 * desc: ”√ªß’Àªß
 */
public class Account {
    private String id;
    private String name;
    private String gender;
    private String token;
    private MLocation location;

    public Account() {}
    public Account(String id, String name, String gender, String token, MLocation location) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MLocation getLocation() {
        return location;
    }

    public void setLocation(MLocation location) {
        this.location = location;
    }
}
