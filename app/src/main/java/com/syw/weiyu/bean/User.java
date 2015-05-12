package com.syw.weiyu.bean;

import java.io.Serializable;

/**
 * Created by songyouwei on 2015/1/30.
 * 用户实体类
 * 用户对象包含的location为自己的MLocation对象
 */
public class User implements Serializable {
    private String id;
    private String name;
    private String gender;
    private String tags;
    private String organization;
    private MLocation location;

    public User() {}
    public User(String userId, String name, String gender, String tags, String organization, MLocation location) {
        this.id = userId;
        this.name = name;
        this.gender = gender;
        this.tags = tags;
        this.organization = organization;
        this.location = location;
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
}
