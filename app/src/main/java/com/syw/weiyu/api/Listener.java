package com.syw.weiyu.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

/**
 * author: youwei
 * date: 2015-05-11
 * desc: 微遇APP内部回调接口
 */
public interface Listener<T> {

    void onSuccess(T data);
    void onFailure(String msg);

}

