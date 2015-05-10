package com.syw.weiyu.util;

import com.syw.weiyu.api.AdApi;
import com.syw.weiyu.api.impl.MoGo;

/**
 * author: songyouwei
 * date: 2015-05-09
 * desc: 简单的IOC
 */
public class IOC {
    private static AdApi adApi;
    public static AdApi getAdApi() {
        if (adApi == null) adApi = new MoGo();
        return adApi;
    }
}
