package com.syw.weiyu.util;

import com.syw.weiyu.api.IAdApi;
import com.syw.weiyu.api.IShuoshuoApi;
import com.syw.weiyu.api.impl.AdMoGo;
import com.syw.weiyu.api.impl.ShuoshuoApi;

/**
 * author: songyouwei
 * date: 2015-05-09
 * desc: 简单的IOC容器，在这里配置接口的具体实现
 */
public class IOC {
    private static IAdApi adApi;
    private static IShuoshuoApi shuoshuoApi;

    public static IAdApi getAdApi() {
        if (adApi == null) adApi = new AdMoGo();
        return adApi;
    }
    public static IShuoshuoApi getShuoshuoApi() {
        if (shuoshuoApi == null) shuoshuoApi = new ShuoshuoApi();
        return shuoshuoApi;
    }
}
