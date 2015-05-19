package com.syw.weiyu.api;

import com.syw.weiyu.bean.MLocation;

/**
 * Created by youwei on 2015/5/7 0007.
 */
public interface ILocationApi {
    void locate();
    void updateUserLocation(Listener listener);
    void getCachedLocation(Listener listener);
}
