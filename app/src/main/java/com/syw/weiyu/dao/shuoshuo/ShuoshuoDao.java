package com.syw.weiyu.dao.shuoshuo;

import com.alibaba.fastjson.JSON;
import com.syw.weiyu.AppConstants;
import com.syw.weiyu.AppException;
import com.syw.weiyu.bean.*;
import com.syw.weiyu.bean.jsonobj.PoiItemJsonObj;
import com.syw.weiyu.bean.jsonobj.ResultJsonObj;
import com.syw.weiyu.dao.location.LocationDao;
import com.syw.weiyu.third.lbs.LBSCloud;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * author: songyouwei
 * date: 2015-05-19
 * desc:
 */
public class ShuoshuoDao {
    public ShuoshuoList getNearByShuoshuoList(int pageIndex) throws AppException {
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
        return parseShuoshuoListFromJson(jsonStr);
    }

    public CommentList getShuoshuoCommentList(long ssId) throws AppException {
        String url = AppConstants.url_list_poi;

        AjaxParams params = LBSCloud.getInitializedParams(AppConstants.geotable_id_comment);
        params.put("q","");
        //按时间|距离排序，优先显示时间靠前的
        params.put("sortby","timestamp:-1|distance:1");
        params.put("ssId",ssId+","+ssId);
        //get
        FinalHttp http = new FinalHttp();
        String jsonStr = (String) http.getSync(url, params);
        return parseCommentListFromJson(jsonStr);
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
    private ShuoshuoList parseShuoshuoListFromJson(String jsonStr) throws AppException {
        ResultJsonObj jsonObj = JSON.parseObject(jsonStr, ResultJsonObj.class);
        if (jsonObj.getStatus()!=0) throw new AppException("附近的说说列表获取出错");
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
        shuoshuoList.setShuoshuoList(list);
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
    private CommentList parseCommentListFromJson(String jsonStr) throws AppException {
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
