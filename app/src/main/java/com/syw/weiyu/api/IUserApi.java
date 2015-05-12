package com.syw.weiyu.api;

import com.syw.weiyu.bean.User;

/**
 * Created by youwei on 2015/5/7 0007.
 */
public interface IUserApi {
    void register(User user);
    User login(User user);
    void logout(String id);
    boolean isOnline(String id);
    int getLastOnlineTimestamp(String id);
    User getUserProfile(String id);
    void updateUserProfile(User user);
}