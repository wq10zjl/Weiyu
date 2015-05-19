package com.syw.weiyu.api;

import com.syw.weiyu.bean.Shuoshuo;

/**
 * author: youwei
 * date: 2015-05-11
 * desc:
 */
public interface IShuoshuoApi {
    void getNearbyShuoshuo();
    void getShuoshuoDetail(String id);
    void publishShuoshuo(Shuoshuo shuoshuo);
}