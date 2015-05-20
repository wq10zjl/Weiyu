package com.syw.weiyu.api;

import com.syw.weiyu.AppException;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.bean.ShuoshuoList;

/**
 * author: youwei
 * date: 2015-05-11
 * desc:
 */
public interface IShuoshuoApi {
    ShuoshuoList refreshNearbyShuoshuo() throws AppException;
    ShuoshuoList getNearbyShuoshuo(int pageIndex) throws AppException;
    Shuoshuo getShuoshuoDetail(Shuoshuo shuoshuoWithoutComments) throws AppException;
    void publishShuoshuo(String content) throws AppException;
}