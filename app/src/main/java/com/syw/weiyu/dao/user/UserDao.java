package com.syw.weiyu.dao.user;

import com.alibaba.fastjson.JSON;
import com.syw.weiyu.AppConstants;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.AppException;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.bean.jsonobj.SingleResultJsonObj;
import com.syw.weiyu.bean.jsonobj.UserPoiItemJsonObj;
import com.syw.weiyu.third.LBSCloud;
import com.syw.weiyu.util.StringUtil;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

/**
 * author: youwei
 * date: 2015-05-29
 * desc: 用户存取类
 */
public class UserDao {

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

    private User parseUserFromJson(String json) throws AppException {
        if (StringUtil.isEmpty(json)) throw new AppException("用户获取出错");
        SingleResultJsonObj obj = JSON.parseObject(json,SingleResultJsonObj.class);
        if (obj.getStatus() != 0) throw new AppException("用户获取出错");
        UserPoiItemJsonObj poi = obj.getPoi();
        User user = new User(poi.getUserId(),poi.getName(),poi.getGender());
        user.setLocation(new MLocation(poi.getCity(),poi.getProvince(),poi.getDistrict()));
        return user;
    }
}
