package com.syw.weiyu.dao.location;

import android.support.annotation.NonNull;
import com.alibaba.fastjson.JSONObject;
import com.syw.weiyu.AppConstants;
import com.syw.weiyu.AppException;
import com.syw.weiyu.api.Listener;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.third.LBSCloud;
import com.syw.weiyu.util.StringUtil;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * author: youwei
 * date: 2015-05-12
 * desc:
 */
public class UserPoiDao {

    /**
     * 在LBS云创建用户POI数据
     * @param user
     * @param location
     * @throws AppException
     */
    public void create(@NonNull User user,MLocation location, final Listener<String> listener) {
        if (location == null) location = new MLocation(null);

        AjaxParams params = LBSCloud.getInitializedParams(AppConstants.geotable_id_user);
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
        final String url = AppConstants.url_create_poi;
        FinalHttp http = new FinalHttp();
        http.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                if (StringUtil.isEmpty(s)) {
                    listener.onCallback(Listener.CallbackType.onFailure,null,"网络异常");
                } else {
                    JSONObject result = JSONObject.parseObject(s);
                    if (result.getInteger("status") == 0 || result.getInteger("status") == 3002) {
                        //创建成功 or 这是老用户（主键重复）
                        listener.onCallback(Listener.CallbackType.onSuccess,null,null);
                    } else {
                        //创建POI出错
                        listener.onCallback(Listener.CallbackType.onFailure,null,"创建用户POI信息出错");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                //创建POI出错
                listener.onCallback(Listener.CallbackType.onFailure, null, strMsg);
            }
        });
    }
}
