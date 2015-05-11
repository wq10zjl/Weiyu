package com.syw.weiyu.util;

import com.syw.weiyu.api.AdApi;
import com.syw.weiyu.api.impl.AdMoGo;

/**
 * author: songyouwei
 * date: 2015-05-09
 * desc: 简单的IOC容器，在这里配置接口的具体实现
 */
public class IOC {
    private static AdApi adApi;
    public static AdApi getAdApi() {
        if (adApi == null) adApi = new AdMoGo();
        return adApi;
    }
}
