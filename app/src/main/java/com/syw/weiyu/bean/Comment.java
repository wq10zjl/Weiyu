package com.syw.weiyu.bean;

/**
 * author: youwei
 * date: 2015-05-20
 * desc: 评论实体类
 */
public class Comment {
    private String userName;
    private String userId;
    private String content;
    private int ssId;
    private long timestamp;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSsId() {
        return ssId;
    }

    public void setSsId(int ssId) {
        this.ssId = ssId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}