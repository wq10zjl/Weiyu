package com.syw.weiyu.dao.shuoshuo;

import com.alibaba.fastjson.JSON;
import com.syw.weiyu.core.AppConstants;
import com.syw.weiyu.core.AppContext;
import com.syw.weiyu.core.AppException;
import com.syw.weiyu.core.Listener;
import com.syw.weiyu.core.Null;
import com.syw.weiyu.bean.*;
import com.syw.weiyu.bean.jsonobj.ShuoshuoItemJsonObj;
import com.syw.weiyu.bean.jsonobj.NearbySearchListJsonObj;
import com.syw.weiyu.third.LBSCloud;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * author: songyouwei
 * date: 2015-05-19
 * desc: 说说数据存取类
 */
public class ShuoshuoDao4LBSyun implements ShuoshuoDao {


    /**
     * 获取附近的说说列表
     * @param pageIndex
     */
    @Override
    public void getNearbyShuoshuos(MLocation location, int pageSize, final int pageIndex, final Listener<ShuoshuoList> listener) {
        String url = AppConstants.url_nearby_search;

        AjaxParams params = LBSCloud.getInitializedParams(AppConstants.geotable_id_shuoshuo);
        params.put("location", location.getLongitude() + "," + location.getLatitude());
        params.put("radius",AppConstants.default_radius);
        //按时间|距离排序，优先显示时间靠前的
        params.put("sortby","timestamp:-1|distance:1");
        params.put("page_index",pageIndex-1+"");//检索是从0页开始的
        params.put("page_size",pageSize+"");
        //getCache
        FinalHttp http = new FinalHttp();
        http.get(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                try {
                    ShuoshuoList shuoshuoList = parseShuoshuoListFromJson(s);
                    listener.onSuccess(shuoshuoList);

                    //cache
                    if (pageIndex == 1) {
                        AppContext.putCache(AppContext.KEY_NEARBYSHUOSHUOS, shuoshuoList);
                    } else {
                        ShuoshuoList oldList = (ShuoshuoList)AppContext.getCache(AppContext.KEY_NEARBYSHUOSHUOS);
                        oldList.getShuoshuos().addAll(shuoshuoList.getShuoshuos());
                        AppContext.putCache(AppContext.KEY_NEARBYSHUOSHUOS, oldList);
                    }
                } catch (AppException e) {
                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                listener.onFailure("获取说说列表失败:"+strMsg);
            }
        });
    }


    /**
     * 发布说说
     * @param content
     * @param listener
     */
    @Override
    public void create(Account account, MLocation location, String content, long timeStamp, final Listener<Null> listener) {
        String url = AppConstants.url_create_poi;

        AjaxParams params = LBSCloud.getInitializedParams(AppConstants.geotable_id_shuoshuo);
        params.put("userId", account.getId());
        params.put("userName", account.getName());

        params.put("longitude", location.getLongitude());
        params.put("latitude", location.getLatitude());
        //content
        params.put("content",content);
        params.put("timestamp", String.valueOf(timeStamp));

        //post
        new FinalHttp().post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                if (JSON.parseObject(s).getInteger("status") == 0) listener.onSuccess(null);
                else listener.onFailure("发送错误:"+s);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                listener.onFailure("发送错误:"+strMsg);
            }
        });
    }

    @Override
    public void addCommentCount(Shuoshuo shuoshuo) {

    }

    @Override
    public void addLikedCount(Shuoshuo shuoshuo) {

    }


    /**
     * 解析包含说说列表的json数据
     * eg:
     {
         "status": 0,
         "total": 35,
         "size": 1,
         "contents": [
             {
             "timestamp": 1432767399619,
             "userName": "魅力四射",
             "uid": 890849790,
             "province": "北京市",
             "geotable_id": 99489,
             "content": "微乐官方免费注册下载网址:http:\/\/www.v89.com 邀请码:18765068625\ 现在赚的就是互联网的钱，要懂得走在时代前沿。 欢迎你加入微乐 ， 注册好了进群学习交流 QQ群:274307262",
             "district": "东城区",
             "create_time": 1432767234,
             "city": "北京市",
             "userId": "865418028527404",
             "location": [
                 116.403874,
                 39.914889
             ],
             "title": null,
             "coord_type": 3,
             "type": 0,
             "distance": 1014058,
             "weight": 0
             }
         ]
     }
     * @param jsonStr
     * @return
     */
    private ShuoshuoList parseShuoshuoListFromJson(String jsonStr) throws AppException {
        NearbySearchListJsonObj jsonObj = new NearbySearchListJsonObj();
        jsonObj = JSON.parseObject(jsonStr, jsonObj.getClass());
        if (jsonObj.getStatus()!=0) throw new AppException("说说获取出错");
        int total = jsonObj.getTotal();
        List<Shuoshuo> list = new ArrayList<>();
        for (int i=0;i<jsonObj.getSize();i++) {
            Shuoshuo shuoshuo = new Shuoshuo();
            ShuoshuoItemJsonObj poi = JSON.parseObject(jsonObj.getContents().get(i).toString(), ShuoshuoItemJsonObj.class);
            shuoshuo.setId(poi.getUid());
            shuoshuo.setTimestamp(poi.getTimestamp());
            shuoshuo.setUserId(poi.getUserId());
            shuoshuo.setUserName(poi.getUserName());
            shuoshuo.setContent(poi.getContent());
            shuoshuo.setLocation(new MLocation(poi.getCity(),poi.getProvince(),poi.getDistrict()));
            list.add(shuoshuo);
        }
        return new ShuoshuoList(total,list);
    }

    /**
     * eg:
     *
       {
         "status": 0,
         "size": 1,
         "total": 1,
         "pois": [
             {
             "title": null,
             "location": [
                     1,
                     1
                ],
             "city": "",
             "create_time": "2015-05-20 15:37:16",
             "geotable_id": 104953,
             "province": "",
             "district": "",
             "ssId": 111,
             "userId": "357143040902071",
             "city_id": 0,
             "id": 870430815
             }
         ],
         "message": "成功"
       }
     * @param jsonStr
     * @return
     */
    private List<Comment> parseCommentsFromJson(String jsonStr) throws AppException {
        NearbySearchListJsonObj jsonObj = JSON.parseObject(jsonStr,NearbySearchListJsonObj.class);
        if (jsonObj.getStatus()!=0) throw new AppException("评论获取出错");
        List<Comment> list = new ArrayList<>();
        for (int i=0;i<jsonObj.getSize();i++) {
            Comment comment = new Comment();
            ShuoshuoItemJsonObj poi = (ShuoshuoItemJsonObj) jsonObj.getContents().get(i);
            comment.setUserId(poi.getUserId());
            comment.setUserName(poi.getUserName());
            comment.setTimestamp(poi.getTimestamp());
            comment.setContent(poi.getContent());
            list.add(comment);
        }
        return list;
    }
}
