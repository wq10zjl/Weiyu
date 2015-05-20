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
    private ShuoshuoList shuoshuoList;
    private ShuoshuoList getShuoshuoList() {
        return shuoshuoList;
    }
    private void setShuoshuoList(ShuoshuoList shuoshuoList) {
        this.shuoshuoList = shuoshuoList;
    }

    /**
     * 刷新（获取第一页的说说）
     * @return
     * @throws AppException
     */
    @Override
    public ShuoshuoList refreshNearbyShuoshuo() throws AppException {
        return getNearbyShuoshuo(0);
    }

    /**
     * 获取附近的说说
     * 优先从内存缓存中获取
     * @param pageIndex
     * @return
     * @throws AppException
     */
    @Override
    public ShuoshuoList getNearbyShuoshuo(int pageIndex) throws AppException {
        ShuoshuoList list = getShuoshuoList();
        if (list == null) {
            list = new ShuoshuoDao().getNearByList(pageIndex);
            setShuoshuoList(list);
        }
        return list;
    }

    /**
     * 获取说说详情
     * @param shuoshuo
     * @return
     * @throws AppException
     */
    @Override
    public Shuoshuo getShuoshuoDetail(Shuoshuo shuoshuo) throws AppException {
        shuoshuo.setCommentList(new ShuoshuoDao().getComments(shuoshuo.getId()));
        return shuoshuo;
    }

    @Override
    public void publishShuoshuo(String content) throws AppException {
        new ShuoshuoDao().add(content);
    }
}
