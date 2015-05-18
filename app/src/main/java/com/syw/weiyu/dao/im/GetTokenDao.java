package com.syw.weiyu.dao.im;

import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.AppException;
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

    /**
     * 向融云服务器注册用户，在回调中拿token
     * <p/>
     * Post请求：userId必选，其余两项可选
     * 请求示例：
     * POST /user/getToken.json HTTP/1.1
     * Host: api.cn.rong.io
     * App-Key: uwd1c0sxdlx2
     * Nonce: 14314
     * Timestamp: 1408706337
     * Signature: 890b422b75c1c5cb706e4f7921df1d94e69c17f4
     * Content-Type: application/x-www-form-urlencoded
     * <p/>
     * userId=jlk456j5&name=Ironman&portraitUri=http%3A%2F%2Fabc.com%2Fmyportrait.jpg
     * <p/>
     * 响应：返回码，200 为正常
     * 响应示例：
     * HTTP/1.1 200 OK
     * Content-Type: application/json; charset=utf-8
     * <p/>
     * {"code":200, "userId":"jlk456j5", "token":"sfd9823ihufi"}
     */
    public String getToken(String userId, String name, String portraitUri) throws AppException {
        //请求参数
        AjaxParams params = new AjaxParams();
        params.put("userId", userId);
        params.put("name", name);
        params.put("portraitUri", portraitUri);
        String jsonResult = (String) RongCloud.getSignedHttp().postSync("https://api.cn.rong.io/user/getToken.json", params);
        JSONObject result = JSON.parseObject(jsonResult);
        if (result.getInteger("code") == 200) {
            return result.getString("token");
        } else {
            throw new AppException("token 获取失败");
        }
    }
}
