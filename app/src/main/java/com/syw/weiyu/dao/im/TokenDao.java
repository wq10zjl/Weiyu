package com.syw.weiyu.dao.im;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.syw.weiyu.AppConstants;

import com.syw.weiyu.api.Listener;
import com.syw.weiyu.third.RongCloud;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * author: youwei
 * date: 2015-05-12
 * desc: 获取Token
 */
public class TokenDao {

    /**
     * 向融云服务器注册用户，在回调中拿token
     * <p/>
     * Post请求：userId必选，其余两项可选
     * 请求示例：
     * POST /user/getCache.json HTTP/1.1
     * Host: api.cn.rong.io
     * AppContext-Key: uwd1c0sxdlx2
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
    public void get(String userId, final String name, String portraitUri, final Listener<String> listener) {
        //请求参数
        AjaxParams params = new AjaxParams();
        params.put("userId", userId);
        params.put("name", name);
        params.put("portraitUri", portraitUri);
        RongCloud.getSignedHttp().post(AppConstants.url_user_gettoken, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                JSONObject result = JSON.parseObject(s);
                if (result.getInteger("code") == 200) {
                    String token = result.getString("token");
                    listener.onSuccess(token);
                } else {
                    listener.onFailure("token获取出错:"+s);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                listener.onFailure("网络错误:"+strMsg);
            }
        });
    }
}
