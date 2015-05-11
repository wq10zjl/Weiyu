package com.syw.weiyu.api;

import android.app.Activity;
import android.view.View;

/**
 * author: songyouwei
 * date: 2015-05-07
 * desc: 广告相关API
 */
public interface AdApi {
    /**
     * 初始化操作，设置参数
     */
    void init();

    /**
     * 获取到的BannerAdView
     * @param activity
     * @param listener
     * @return
     */
    View getBannerAdView(Activity activity,Listener listener);

    /**
     * 显示开屏广告
     * @param activity
     * @param listener
     */
    void showSplashAd(Activity activity,Listener listener);

}
