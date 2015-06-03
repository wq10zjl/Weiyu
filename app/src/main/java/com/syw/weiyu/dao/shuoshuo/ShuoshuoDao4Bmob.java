package com.syw.weiyu.dao.shuoshuo;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.api.Listener;
import com.syw.weiyu.api.Null;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.bean.ShuoshuoList;
import net.tsz.afinal.FinalDb;

import java.util.List;

/**
 * author: youwei
 * date: 2015-06-03
 * desc:
 */
public class ShuoshuoDao4Bmob implements ShuoshuoDao {
    @Override
    public void getNearbyShuoshuos(final MLocation location,final int pageSize,final int pageIndex, final Listener<ShuoshuoList> listener) {
        BmobQuery<Shuoshuo> countQuery = new BmobQuery<>();
        countQuery.count(AppContext.getCtx(), Shuoshuo.class, new CountListener() {
            @Override
            public void onSuccess(final int i) {
                BmobGeoPoint gpsAdd = new BmobGeoPoint(Double.parseDouble(location.getLongitude()), Double.parseDouble(location.getLatitude()));
                BmobQuery<Shuoshuo> bmobQuery = new BmobQuery<>();
                bmobQuery.addWhereNear("gpsAdd", gpsAdd);
                bmobQuery.setLimit(pageSize);//获取最接近用户地点的n条数据
                bmobQuery.setSkip((pageIndex - 1) * pageSize);
                bmobQuery.findObjects(AppContext.getCtx(), new FindListener<Shuoshuo>() {
                    @Override
                    public void onSuccess(List<Shuoshuo> list) {
                        ShuoshuoList shuoshuoList = new ShuoshuoList(i,list);
                        listener.onSuccess(shuoshuoList);

                        //cache
                        if (pageIndex == 1) {
                            AppContext.putCache(AppContext.KEY_NEARBYSHUOSHUOS, shuoshuoList);
                        } else {
                            ShuoshuoList oldList = (ShuoshuoList)AppContext.getCache(AppContext.KEY_NEARBYSHUOSHUOS);
                            oldList.getShuoshuos().addAll(shuoshuoList.getShuoshuos());
                            AppContext.putCache(AppContext.KEY_NEARBYSHUOSHUOS, oldList);
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        listener.onFailure("获取说说列表失败:"+s);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure("获取说说列表失败:"+s);
            }
        });
    }

    @Override
    public void create(Account account, MLocation location, String content, long timeStamp, final Listener<Null> listener) {
        Shuoshuo shuoshuo = new Shuoshuo();
        shuoshuo.setId(shuoshuo.hashCode());//or null
        shuoshuo.setUserId(account.getId());
        shuoshuo.setUserName(account.getName());
        BmobGeoPoint gpsAdd = new BmobGeoPoint(Double.parseDouble(location.getLongitude()), Double.parseDouble(location.getLatitude()));
        shuoshuo.setGpsAdd(gpsAdd);
        shuoshuo.setAddressStr(location.getAddress());
        shuoshuo.setContent(content);
        shuoshuo.setTimestamp(timeStamp);
        shuoshuo.save(AppContext.getCtx(), new SaveListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess(null);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure("发送出现错误:"+s);
            }
        });
    }
}
