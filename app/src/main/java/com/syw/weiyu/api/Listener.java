package com.syw.weiyu.api;

/**
 * author: youwei
 * date: 2015-05-11
 * desc: API的监听接口
 */
public abstract class Listener {

    /**
     * 回调
     * @param callback 回调类型
     * @param msg 包含的消息
     */
    public abstract void onCallback(Callback callback, String msg);

    /**
     * 回调类型
     */
    public enum Callback {
        //IAdApi
        onAdError,onAdClick,onAdClose,
        //IUserApi
        onUserRegisterSuccess,getOnUserRegisterFailure,
        //ILocationApi
        //IShuoshuoApi
    }
}