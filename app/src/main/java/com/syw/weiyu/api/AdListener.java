package com.syw.weiyu.api;

/**
 * author: songyouwei
 * date: 2015-05-07
 * desc: 广告相关监听接口
 */
public interface AdListener {

    /**
     * 当关闭时
     */
    void onClose();

    /**
     * 当错误时
     * @param msg 错误消息
     */
    void onError(String msg);

    /**
     * 当点击时
     * @param msg 消息
     */
    void onClick(String msg);
}
