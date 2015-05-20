package com.syw.weiyu.api.impl;

import com.syw.weiyu.AppException;
import com.syw.weiyu.api.IShuoshuoApi;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.bean.ShuoshuoList;
import com.syw.weiyu.dao.shuoshuo.ShuoshuoDao;

/**
 * author: songyouwei
 * date: 2015-05-19
 * desc: 说说接口实现
 */
public class ShuoshuoApi implements IShuoshuoApi {
    @Override
    public ShuoshuoList refreshNearbyShuoshuo() throws AppException {
        return getNearbyShuoshuo(0);
    }

    @Override
    public ShuoshuoList getNearbyShuoshuo(int pageIndex) throws AppException {
        return new ShuoshuoDao().getNearByShuoshuoList(pageIndex);
    }

    @Override
    public Shuoshuo getShuoshuoDetail(Shuoshuo shuoshuo) throws AppException {
        shuoshuo.setCommentList(new ShuoshuoDao().getShuoshuoCommentList(shuoshuo.getId()));
        return shuoshuo;
    }

    @Override
    public void publishShuoshuo(Shuoshuo shuoshuo) {

    }
}
