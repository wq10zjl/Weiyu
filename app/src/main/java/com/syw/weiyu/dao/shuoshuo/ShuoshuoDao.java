package com.syw.weiyu.dao.shuoshuo;

import com.syw.weiyu.api.Listener;
import com.syw.weiyu.api.Null;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.bean.ShuoshuoList;

/**
 * author: youwei
 * date: 2015-06-03
 * desc:
 */
public interface ShuoshuoDao {
    /**
     * 获取附近的说说列表
     * @param location
     * @param pageSize
     * @param pageIndex
     * @param listener
     */
    void getNearbyShuoshuos(MLocation location,int pageSize,int pageIndex,Listener<ShuoshuoList> listener);

    /**
     * 发布说说
     * @param account
     * @param location
     * @param content
     * @param timeStamp
     * @param listener
     */
    void create(Account account,MLocation location,String content,long timeStamp, final Listener<Null> listener);

    /**
     * 评论数++
     * @param shuoshuo
     */
    void addCommentCount(Shuoshuo shuoshuo);

    /**
     * 喜欢数++
     * @param shuoshuo
     */
    void addLikedCount(Shuoshuo shuoshuo);
}
