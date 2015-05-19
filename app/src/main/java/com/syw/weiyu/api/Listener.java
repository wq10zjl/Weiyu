package com.syw.weiyu.api;

import java.util.Objects;

/**
 * author: youwei
 * date: 2015-05-11
 * desc: API的监听接口
 */
public abstract class Listener {

    /**
     * 回调
     * @param callbackType 回调类型
     * @param message 返回的消息
     */
    public abstract <T> void onCallback(CallbackType callbackType,T message);

    /**
     * 回调类型
     */
    public enum CallbackType {
        //IAdApi
        onAdError,onAdClick,onAdClose,
        //IUserApi
        onUserRegisterSuccess,getOnUserRegisterFailure,
        //ILocationApi
        onLocateSuccess,onLocateError,
        //IShuoshuoApi
    }
}