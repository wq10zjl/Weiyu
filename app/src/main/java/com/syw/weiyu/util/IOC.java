package com.syw.weiyu.util;

import com.syw.weiyu.api.IAdApi;
import com.syw.weiyu.api.impl.AdMoGo;

/**
 * author: songyouwei
 * date: 2015-05-09
 * desc: 简单的IOC容器，在这里配置接口的具体实现
 */
public class IOC {
    private static IAdApi adApi;
    public static IAdApi getAdApi() {
        if (adApi == null) adApi = new AdMoGo();
        return adApi;
    }
}
