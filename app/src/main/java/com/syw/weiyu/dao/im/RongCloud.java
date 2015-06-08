package com.syw.weiyu.dao.im;

import android.support.annotation.NonNull;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;
import com.syw.weiyu.core.AppConstants;
import com.syw.weiyu.core.AppException;

import com.syw.weiyu.core.Listener;
import com.syw.weiyu.core.Null;
import com.syw.weiyu.util.StringUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import net.tsz.afinal.FinalHttp;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

/**
 * author: youwei
 * date: 2015-05-12
 * desc: 融云的一些基础操作
 */
public class RongCloud {

    public static void connect(@NonNull String token) throws AppException {
        if (StringUtil.isEmpty(token)) throw new AppException("无token");
        // 连接融云服务器。
        try {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Logger.d("Connect rongcloud failure, onTokenIncorrect");
                }

                @Override
                public void onSuccess(String s) {
                    // 此处处理连接成功。
                    Logger.d("Connect rongcloud success.");
                    //设置其它的监听器
                    RongCloudEvent.getInstance().setOtherListener();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    // 此处处理连接错误。
                     Logger.e("Connect rongcloud failed, errorMsg:"+errorCode.getMessage()+" errorValue:"+errorCode.getValue());
                }
            });
        } catch (Exception e) {
            Logger.e("Connect rongcloud exception:"+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 注销当前登录,执行该方法后不会再收到 push 消息。
     */
    public static void disconnect() {
        RongIM.getInstance().logout();
    }

    /**
     * 向融云服务器注册用户，在回调中拿token
     * <p/>
     * Post请求：userId必选，其余两项可选
     * 请求示例：
     * POST /user/getCache.json HTTP/1.1
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
    public static void getToken(String userId, final String name, String portraitUri, final Listener<String> listener) {
        //请求参数
        AjaxParams params = new AjaxParams();
        params.put("userId", userId);
        params.put("name", name);
        params.put("portraitUri", portraitUri);
        RongCloud.getSignedHttp().post(AppConstants.url_user_gettoken, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                JSONObject result = JSON.parseObject(s);
                if (result!=null && result.getInteger("code") == 200) {
                    String token = result.getString("token");
                    listener.onSuccess(token);
                } else {
                    listener.onFailure("token获取出错:" + s);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                listener.onFailure("网络错误:" + strMsg);
            }
        });
    }

    public static void refreshUserInfo(String userId, final String name, String portraitUri, final Listener<Null> listener) {
        AjaxParams params = new AjaxParams();
        params.put("userId", userId);
        params.put("name", name);
        params.put("portraitUri", portraitUri);
        RongCloud.getSignedHttp().post(AppConstants.url_user_refresh, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                JSONObject result = JSON.parseObject(s);
                if (result!=null && result.getInteger("code") == 200) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure("用户资料刷新出错:" + s);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                listener.onFailure("网络错误:" + strMsg);
            }
        });
    }

    /**
     * 得到已添加了请求签名Header的FinalHttp
     * @return
     */
    public static FinalHttp getSignedHttp() {

        String appKey = AppConstants.rongcloud_app_key;
        String appSecret = AppConstants.rongcloud_app_secret;
        String nonce = String.valueOf(Math.random() * 1000000);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        StringBuffer toSign = new StringBuffer(appSecret).append(nonce).append(timestamp);
        String signature = sha1(toSign.toString());

        FinalHttp http = new FinalHttp();
        http.addHeader("App-Key",appKey);
        http.addHeader("Nonce",nonce);
        http.addHeader("Timestamp",timestamp);
        http.addHeader("Signature",signature);

        return http;
    }

    /**
     * SHA1 hash算法
     * @param value
     * @return
     */
    private static String sha1(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(value.getBytes("utf-8"));
            byte[] digest = md.digest();
            return String.valueOf(Hex.encodeHex(digest));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
