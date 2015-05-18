package com.syw.weiyu.dao.location;

import com.alibaba.fastjson.JSONObject;
import com.syw.weiyu.AppException;
import com.syw.weiyu.R;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.third.lbs.LBSCloud;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

/**
 * author: youwei
 * date: 2015-05-12
 * desc:
 */
public class CreateUserPoiDao {
    public void create(User user,MLocation location) throws AppException {
        AjaxParams params = LBSCloud.getInitializedParams("93391");
        //user info
        params.put("userId", user.getId());
        params.put("name", user.getName());
        params.put("gender", user.getGender());
        params.put("tags", user.getTags());
        params.put("organization", user.getOrganization());
        //location info
        params.put("title", user.getName());//title使用user.name
        params.put("address", location.getAddress());
        params.put("longitude", location.getLongitude());
        params.put("latitude", location.getLatitude());

        //post
        final String url = "http://api.map.baidu.com/geodata/v3/poi/create";
        FinalHttp http = new FinalHttp();
        String jsonResult = (String) http.postSync(url, params);
        JSONObject result = JSONObject.parseObject(jsonResult);
        if (result.getInteger("status") == 0 || result.getInteger("status") == 3002) {

        } else {
            throw new AppException("用户POI创建失败");
        }
    }
}
