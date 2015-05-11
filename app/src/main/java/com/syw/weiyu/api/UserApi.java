package com.syw.weiyu.api;

/**
 * Created by youwei on 2015/5/7 0007.
 */
public interface UserApi {
    void register();
    void login();
    void logout();
    boolean isOnline();
    boolean getLastOnlineTime();
    void getUserProfile();
    void updateUserProfile();
}