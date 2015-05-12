package com.syw.weiyu.dao.im;

import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.R;
import com.syw.weiyu.third.lbs.LBSCloud;
import com.syw.weiyu.ui.main.MainTabsActivity;
import com.syw.weiyu.util.ACache;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import de.greenrobot.event.EventBus;

/**
 * author: youwei
 * date: 2015-05-12
 * desc:
 */
public class GetTokenDao {

    public String getToken(String userId, String name, String portraitUri) {
        //请求参数
        AjaxParams params = new AjaxParams();
        params.put("userId",userId);
        params.put("name",name);
        params.put("portraitUri", portraitUri);
        String jsonResult = (String) RongCloud.getSignedHttp().postSync("https://api.cn.rong.io/user/getToken.json", params);
        return null;
    }
}
