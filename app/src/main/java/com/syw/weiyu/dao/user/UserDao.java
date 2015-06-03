package com.syw.weiyu.dao.user;

import android.support.annotation.NonNull;
import com.syw.weiyu.AppException;
import com.syw.weiyu.api.Listener;
import com.syw.weiyu.api.Null;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.bean.UserList;

/**
 * author: youwei
 * date: 2015-06-03
 * desc:
 */
public interface UserDao {
    /**
     * 在LBS云创建用户POI数据
     * @param id
     * @param name
     * @param gender
     * @param location
     * @param listener
     */
    void create(String id,String name,String gender,MLocation location,Listener<Null> listener);

    /**
     * 修改用户资料
     * @param id
     * @param name
     * @param gender
     * @param location
     * @param listener
     */
    void update(@NonNull String id,String name,String gender,MLocation location,Listener<Null> listener);

    /**
     * 获取附近的人，获取了数据之后，把用户数据存在DB里
     * @param location
     * @param pageSize
     * @param pageIndex
     * @param listener
     */
    void getNearbyUsers(MLocation location,int pageSize,int pageIndex,Listener<UserList> listener);

    /**
     * 获取用户信息，优先从DB获取
     * @param id
     * @return
     * @throws AppException
     */
    User getUser(String id) throws AppException;
}