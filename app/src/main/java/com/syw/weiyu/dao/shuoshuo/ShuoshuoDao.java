package com.syw.weiyu.dao.shuoshuo;

import com.alibaba.fastjson.JSON;
import com.syw.weiyu.AppConstants;
import com.syw.weiyu.AppException;
import com.syw.weiyu.bean.*;
import com.syw.weiyu.bean.jsonobj.PoiItemJsonObj;
import com.syw.weiyu.bean.jsonobj.ResultJsonObj;
import com.syw.weiyu.dao.location.LocationDao;
import com.syw.weiyu.dao.user.AccountDao;
import com.syw.weiyu.third.lbs.LBSCloud;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * author: songyouwei
 * date: 2015-05-19
 * desc: 说说数据存取类
 */
public class ShuoshuoDao {

    /**
     * 获取附近的说说列表
     * @param pageIndex
     * @return
     * @throws AppException
     */
    public ShuoshuoList getNearByList(int pageIndex) throws AppException {
        String url = AppConstants.url_nearby_search;

        AjaxParams params = LBSCloud.getInitializedParams(AppConstants.geotable_id_shuoshuo);
        params.put("q","");
        params.put("location", new LocationDao().get().getLongitude() + "," + new LocationDao().get().getLatitude());
        params.put("tags","");
        params.put("radius",AppConstants.default_radius);
        //按时间|距离排序，优先显示时间靠前的
        params.put("sortby","timestamp:-1|distance:1");
        params.put("page_index",pageIndex+"");
        params.put("page_size",AppConstants.page_size_default);
        //get
        FinalHttp http = new FinalHttp();
        String jsonStr = (String) http.getSync(url, params);
        return parseListFromJson(jsonStr);
    }

    /**
     * 获取评论列表
     * @param ssId 说说ID
     * @return
     * @throws AppException
     */
    public CommentList getComments(long ssId) throws AppException {
        String url = AppConstants.url_list_poi;

        AjaxParams params = LBSCloud.getInitializedParams(AppConstants.geotable_id_comment);
        params.put("q","");
        //按时间|距离排序，优先显示时间靠前的
        params.put("sortby","timestamp:-1|distance:1");
        params.put("ssId",ssId+","+ssId);
        //get
        FinalHttp http = new FinalHttp();
        String jsonStr = (String) http.getSync(url, params);
        return parseCommentsFromJson(jsonStr);
    }

    /**
     * 发布说说
     * @param content
     * @throws AppException
     */
    public void add(String content) throws AppException {
        String url = AppConstants.url_create_poi;

        AjaxParams params = LBSCloud.getInitializedParams(AppConstants.geotable_id_shuoshuo);
        //account info
        Account user = new AccountDao().get();
        params.put("userId", user.getId());
        params.put("userName", user.getName());
        //location info
        MLocation location = new LocationDao().get();
        params.put("longitude", location.getLongitude());
        params.put("latitude", location.getLatitude());
        //content
        params.put("content",content);
        //timestamp, same as create_time
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));

        //post
        FinalHttp http = new FinalHttp();
        String result = (String)http.postSync(url, params);

        if (JSON.parseObject(result).getInteger("status") != 0) throw new AppException("服务器开小差啦");
    }

    /**
     * 解析包含说说列表的json数据
     * eg:
       {
         "status": 0,
         "size": 2,
         "total": 23,
         "pois": [
             {
             "title": null,
             "location": [
                 120.247089,
                 30.21482
                 ],
             "city": "杭州市",
             "create_time": "2015-05-19 07:55:47",
             "geotable_id": 99489,
             "province": "浙江省",
             "district": "萧山区",
             "timestamp": 1431975265127,
             "content": "怎么没人，不好玩",
             "userName": "哭着笑痛",
             "userId": "865175024792115",
             "city_id": 179,
             "id": 865735044
             }
         ],
         "message": "成功"
       }
     * @param jsonStr
     * @return
     */
    private ShuoshuoList parseListFromJson(String jsonStr) throws AppException {
        ResultJsonObj jsonObj = JSON.parseObject(jsonStr, ResultJsonObj.class);
        if (jsonObj.getStatus()!=0) throw new AppException("服务器开小差啦");
        List<Shuoshuo> list = new ArrayList<>();
        for (int i=0;i<jsonObj.getSize();i++) {
            Shuoshuo shuoshuo = new Shuoshuo();
            PoiItemJsonObj poi = jsonObj.getPois().get(i);
            shuoshuo.setId(poi.getId());
            shuoshuo.setTimestamp(poi.getTimestamp());
            shuoshuo.setUserId(poi.getUserId());
            shuoshuo.setUserName(poi.getUserName());
            shuoshuo.setContent(poi.getContent());
            shuoshuo.setLocation(new MLocation(poi.getCity(),poi.getProvince(),poi.getDistrict()));
            list.add(shuoshuo);
        }
        ShuoshuoList shuoshuoList = new ShuoshuoList();
        shuoshuoList.set(list);
        return null;
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
    private CommentList parseCommentsFromJson(String jsonStr) throws AppException {
        ResultJsonObj jsonObj = JSON.parseObject(jsonStr,ResultJsonObj.class);
        if (jsonObj.getStatus()!=0) throw new AppException("评论获取出错");
        List<Comment> list = new ArrayList<>();
        for (int i=0;i<jsonObj.getSize();i++) {
            Comment comment = new Comment();
            PoiItemJsonObj poi = jsonObj.getPois().get(i);
            comment.setUserId(poi.getUserId());
            comment.setUserName(poi.getUserName());
            comment.setTimestamp(poi.getTimestamp());
            comment.setContent(poi.getContent());
            list.add(comment);
        }
        CommentList commentList = new CommentList();
        commentList.setCommentList(list);
        return commentList;
    }
}
