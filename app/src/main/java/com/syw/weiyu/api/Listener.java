package com.syw.weiyu.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

/**
 * author: youwei
 * date: 2015-05-11
 * desc: API的监听接口
 */
public abstract class Listener<T> {

    /**
     * 回调
     * @param callbackType 回调类型
     * @param data 返回的数据
     * @param msg 消息
     */
    public abstract void onCallback(@NonNull CallbackType callbackType,@Nullable T data,@Nullable String msg);

    /**
     * 回调类型
     */
    public enum CallbackType {
        //common
        onSuccess,onFailure,
        //ad
        onAdError,onAdClick,onAdClose,
    }
}