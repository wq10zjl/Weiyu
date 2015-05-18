package com.syw.weiyu.api.impl;

import com.syw.weiyu.AppException;
import com.syw.weiyu.api.IUserApi;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.dao.im.GetTokenDao;
import com.syw.weiyu.dao.location.CreateUserPoiDao;
import com.syw.weiyu.third.lbs.LBSCloud;

/**
 * author: youwei
 * date: 2015-05-12
 * desc: 用户接口实现
 */
public class UserApi implements IUserApi {

    /**
     * 注册接口
     * 1.在LBS云创建POI节点
     * 2.连接IM服务器
     * @param user
     */
    @Override
    public void register(User user) {
        try {
            String token = new GetTokenDao().getToken(user.getId(),user.getName(),null);
            new CreateUserPoiDao().create(user,null);
        } catch (AppException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录接口
     * 1.更新POI位置信息
     * 2.连接IM服务器
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return null;
    }

    /**
     * 登出
     * 1.更新最后在线时间
     * 2.登出IM服务器
     * @param id
     */
    @Override
    public void logout(String id) {

    }

    /**
     * 是否在线
     * @param id
     * @return
     */
    @Override
    public boolean isOnline(String id) {
        return false;
    }

    /**
     * 获取最后在线时间
     * @param id
     * @return
     */
    @Override
    public int getLastOnlineTimestamp(String id) {
        return 0;
    }

    /**
     * 获取用户资料
     * @param id
     * @return
     */
    @Override
    public User getUserProfile(String id) {
        return null;
    }

    /**
     * 更新用户资料
     */
    @Override
    public void updateUserProfile(User user) {

    }
}
