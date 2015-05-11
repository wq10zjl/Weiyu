package com.syw.weiyu.api;

/**
 * author: youwei
 * date: 2015-05-11
 * desc: API的监听接口
 */
public interface Listener {

    /**
     * 回调
     * @param callback 回调类型
     * @param msg 包含的消息
     */
    void onCallback(Callback callback,String msg);

    /**
     * 回调类型
     */
    enum Callback {
        //AdApi
        onAdError,onAdClick,onAdClose,
        //UserApi
        //LocationApi
        //ShuoshuoApi
    }
}