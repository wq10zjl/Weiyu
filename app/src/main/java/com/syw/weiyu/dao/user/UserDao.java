package com.syw.weiyu.dao.user;

import com.alibaba.fastjson.JSON;
import com.syw.weiyu.AppConstants;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.AppException;
import com.syw.weiyu.api.Listener;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.bean.UserList;
import com.syw.weiyu.bean.jsonobj.NearbySearchListJsonObj;
import com.syw.weiyu.bean.jsonobj.SinglePoiJsonObj;
import com.syw.weiyu.bean.jsonobj.UserItemJsonObj;
import com.syw.weiyu.dao.location.LocationDao;
import com.syw.weiyu.third.LBSCloud;
import com.syw.weiyu.util.StringUtil;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * author: youwei
 * date: 2015-05-29
 * desc: 用户存取类
 */
public class UserDao {


    /**
     * {
         "status": 0,
         "total": 305,
         "size": 1,
         "contents": [
             {
             "address": "北京市东城区东长安街",
             "city": "北京市",
             "create_time": 1425100947,
             "district": "东城区",
             "gender": "男",
             "geotable_id": 93391,
             "location": [
                 116.403874,
                 39.914889
                 ],
             "modify_time": 1425207832,
             "name": "格拉纸",
             "province": "北京市",
             "title": "格拉纸",
             "userId": "990004484010701",
             "uid": 674291726,
             "coord_type": 3,
             "type": 0,
             "distance": 2031,
             "weight": 0
             }
         ]
     }
     * @param listener
     */
    public void getNearbyUsers(final int pageIndex, final Listener<UserList> listener) {
        final String url = AppConstants.url_nearby_search;

        AjaxParams params = LBSCloud.getInitializedParams(AppConstants.geotable_id_user);
        MLocation location =new LocationDao().get();
        params.put("location",location.getLongitude()+","+location.getLatitude());
        params.put("radius",AppConstants.default_radius);
//        params.put("sortby",sortby);
        params.put("page_index",pageIndex+"");
        params.put("page_size",AppConstants.page_size_default);
        //get
        FinalHttp http = new FinalHttp();
        http.get(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                UserList userList = null;
                try {
                    userList = parseUserListFromJson(s);
                    listener.onSuccess(userList);

                    //save to db
                    FinalDb finalDb = FinalDb.create(AppContext.getCtx());
                    for (User user : userList.getUsers()) {
                        finalDb.save(user);
                    }
                } catch (AppException e) {
                    listener.onFailure("获取附近的人失败:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                listener.onFailure("获取附近的人失败:" + strMsg);
            }
        });
    }

    /**
     * {
         "status": 0,
         "poi": {
             "title": "有緣份的微遇",
             "location": [
                 116.403874,
                 39.914889
         ],
         "city": "北京市",
         "create_time": "2015-05-26 15:05:06",
         "geotable_id": 93391,
         "address": "北京市东城区东长安街",
         "province": "北京市",
         "district": "东城区",
         "gender": "男",
         "name": "有緣份的微遇",
         "userId": "354435050697838",
         "city_id": 131,
         "id": 886091851
         },
         "message": "成功"
     }
     * @param id
     */
    public User getUser(String id) throws AppException {
        FinalDb finalDb = FinalDb.create(AppContext.getCtx());
        User user = finalDb.findById(id, User.class);
        if (user == null) {
            final String url = AppConstants.url_detail_poi;
            AjaxParams params = LBSCloud.getInitializedParams(AppConstants.geotable_id_user);
            params.put("userId",id);

            //get
            FinalHttp http = new FinalHttp();
            String s = (String) http.getSync(url, params);
            user = parseUserFromJson(s);
        }
        return user;
    }

    /**
     * 存储用户信息至db
     * @param user
     */
    public void setUser(User user) {
        FinalDb finalDb = FinalDb.create(AppContext.getCtx());
        finalDb.save(user);
    }

    private User parseUserFromJson(String json) throws AppException {
        if (StringUtil.isEmpty(json)) throw new AppException("用户获取出错");
        SinglePoiJsonObj obj = JSON.parseObject(json, SinglePoiJsonObj.class);
        if (obj.getStatus() != 0) throw new AppException("用户获取出错");
        UserItemJsonObj poi = obj.getPoi();
        User user = new User(poi.getUserId(),poi.getName(),poi.getGender());
        user.setLocation(new MLocation(poi.getCity(), poi.getProvince(), poi.getDistrict(), poi.getAddress()));
        return user;
    }

    private UserList parseUserListFromJson(String jsonStr) throws AppException {
        NearbySearchListJsonObj jsonObj = JSON.parseObject(jsonStr, NearbySearchListJsonObj.class);
        if (jsonObj.getStatus()!=0) throw new AppException("附近的人获取出错");
        int total = jsonObj.getTotal();
        List<User> list = new ArrayList<>();
        for (int i=0;i<jsonObj.getSize();i++) {
            User user = new User();
            UserItemJsonObj poi = JSON.parseObject(jsonObj.getContents().get(i).toString(),UserItemJsonObj.class);
            user.setId(poi.getUserId());
            user.setName(poi.getName());
            user.setGender(poi.getGender());
            user.setLocation(new MLocation(poi.getCity(), poi.getProvince(), poi.getDistrict()));
            list.add(user);
        }
        return new UserList(total,list);
    }

}
