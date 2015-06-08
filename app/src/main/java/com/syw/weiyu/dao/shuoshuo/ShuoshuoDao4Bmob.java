package com.syw.weiyu.dao.shuoshuo;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import com.syw.weiyu.core.App;
import com.syw.weiyu.core.AppConstants;
import com.syw.weiyu.core.Listener;
import com.syw.weiyu.core.Null;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.bean.ShuoshuoList;

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
        countQuery.count(App.getCtx(), Shuoshuo.class, new CountListener() {
            @Override
            public void onSuccess(final int i) {
                BmobGeoPoint gpsAdd = new BmobGeoPoint(Double.parseDouble(location.getLongitude()), Double.parseDouble(location.getLatitude()));
                BmobQuery<Shuoshuo> bmobQuery = new BmobQuery<>();
//                bmobQuery.addWhereNear("gpsAdd", gpsAdd);
                bmobQuery.addWhereWithinKilometers("gpsAdd", gpsAdd, Integer.parseInt(AppConstants.default_kilometers));
                bmobQuery.order("-timestamp");//时间近的靠前
                bmobQuery.setLimit(pageSize);//获取最接近用户地点的n条数据
                bmobQuery.setSkip((pageIndex - 1) * pageSize);
                bmobQuery.findObjects(App.getCtx(), new FindListener<Shuoshuo>() {
                    @Override
                    public void onSuccess(List<Shuoshuo> list) {
                        ShuoshuoList shuoshuoList = new ShuoshuoList(i,list);
                        listener.onSuccess(shuoshuoList);

                        //cache
                        if (pageIndex == 1) {
                            App.putCache(App.KEY_NEARBYSHUOSHUOS, shuoshuoList);
                        } else {
                            ShuoshuoList oldList = (ShuoshuoList) App.getCache(App.KEY_NEARBYSHUOSHUOS);
                            oldList.getShuoshuos().addAll(shuoshuoList.getShuoshuos());
                            App.putCache(App.KEY_NEARBYSHUOSHUOS, oldList);
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
    public void getUserShuoshuos(final String userId,final int pageSize,final int pageIndex, final Listener<ShuoshuoList> listener) {
        BmobQuery<Shuoshuo> countQuery = new BmobQuery<>();
        countQuery.count(App.getCtx(), Shuoshuo.class, new CountListener() {
            @Override
            public void onSuccess(final int i) {
                BmobQuery<Shuoshuo> bmobQuery = new BmobQuery<>();
//                bmobQuery.addWhereNear("gpsAdd", gpsAdd);
                bmobQuery.addWhereEqualTo("userId",userId);
                bmobQuery.order("-timestamp");//时间近的靠前
                bmobQuery.setLimit(pageSize);//获取最接近用户地点的n条数据
                bmobQuery.setSkip((pageIndex - 1) * pageSize);
                bmobQuery.findObjects(App.getCtx(), new FindListener<Shuoshuo>() {
                    @Override
                    public void onSuccess(List<Shuoshuo> list) {
                        ShuoshuoList shuoshuoList = new ShuoshuoList(i,list);
                        listener.onSuccess(shuoshuoList);
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
        shuoshuo.setId(System.currentTimeMillis()+shuoshuo.hashCode());
        shuoshuo.setUserId(account.getId());
        shuoshuo.setUserName(account.getName());
        BmobGeoPoint gpsAdd = new BmobGeoPoint(Double.parseDouble(location.getLongitude()), Double.parseDouble(location.getLatitude()));
        shuoshuo.setGpsAdd(gpsAdd);
        shuoshuo.setAddressStr(location.getAddress());
        shuoshuo.setContent(content);
        shuoshuo.setTimestamp(timeStamp);
        shuoshuo.save(App.getCtx(), new SaveListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess(null);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure("发送说说出现错误:"+s);
            }
        });
    }

    @Override
    public void addCommentCount(Shuoshuo shuoshuo) {
        shuoshuo.increment("commentCount");
        shuoshuo.update(App.getCtx());
    }

    @Override
    public void addLikedCount(Shuoshuo shuoshuo) {
        shuoshuo.increment("likedCount");
        shuoshuo.update(App.getCtx());
    }
}
