package com.syw.weiyu.third.im;

import android.content.Context;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.syw.weiyu.R;
import com.syw.weiyu.util.ACache;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by songyouwei on 2015/2/9.
 * App暂时不做Server端，此类即本地Server，做与融云服务器的一些操作
 */
public class RongCloud {

    private static Context context;
    private static RongCloud rongCloud;
    private RongCloud() {}
    public static RongCloud getInstance(Context ctx) {
        context = ctx;
        if (rongCloud == null) {
            synchronized (RongCloud.class) {
                if (rongCloud == null) {
                    rongCloud = new RongCloud();
                }
            }
        }
        return rongCloud;
    }

    /**
     * 向融云服务器注册用户，在回调中拿token
     *
     Post请求：userId必选，其余两项可选
     请求示例：
     POST /user/get.json HTTP/1.1
     Host: api.cn.rong.io
     App-Key: uwd1c0sxdlx2
     Nonce: 14314
     Timestamp: 1408706337
     Signature: 890b422b75c1c5cb706e4f7921df1d94e69c17f4
     Content-Type: application/x-www-form-urlencoded

     userId=jlk456j5&name=Ironman&portraitUri=http%3A%2F%2Fabc.com%2Fmyportrait.jpg

     响应：返回码，200 为正常
     响应示例：
     HTTP/1.1 200 OK
     Content-Type: application/json; charset=utf-8

     {"code":200, "userId":"jlk456j5", "token":"sfd9823ihufi"}

     * @param userId required
     * @param name
     * @param portraitUri
     * @param callback 回调接口，成功则含token
     */
    public void getToken(String userId, String name, String portraitUri, AjaxCallBack<String> callback) {

        //请求参数
        AjaxParams params = new AjaxParams();
        params.put("userId",userId);
        params.put("name",name);
        params.put("portraitUri",portraitUri);
        Log.d("Weiyu", "get, params:" + params.getParamString());
        getSignedHttp().post(context.getString(R.string.url_user_gettoken), params, callback);
    }

    /**
     * 刷新用户信息
     * 说明：当您的用户昵称和头像变更时，
     *      您的 App Server 应该调用此接口刷新在融云侧保存的用户信息，
     *      以便融云发送推送消息的时候，能够正确显示用户信息。
     * 注意：刷新此信息不会更新客户端的用户信息
     * @param userId
     * @param name
     * @param portraitUri
     * @param callback
     */
    public void refresh(String userId, String name, String portraitUri, AjaxCallBack<String> callback) {
        //请求参数
        AjaxParams params = new AjaxParams();
        params.put("userId",userId);
        params.put("name",name);
        params.put("portraitUri",portraitUri);

        getSignedHttp().post(context.getString(R.string.url_user_refresh), params, callback);
    }

    /**
     * 连接融云服务器
     */
    public void connectRongCloud() {

        String token = ACache.getPermanence(context).getAsString("token");
        // 连接融云服务器。
        try {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                @Override
                public void onSuccess(String s) {
                    // 此处处理连接成功。
                    Logger.d("Connect rongcloud success.");
                    //设置其它的监听器
                    RongCloudEvent.getInstance().setOtherListener();
                }

                @Override
                public void onError(ErrorCode errorCode) {
                    // 此处处理连接错误。
                    Logger.e("Connect rongcloud failed, errormsg:"+errorCode.getMessage());
                }
            });
        } catch (Exception e) {
            Logger.e("Connect rongcloud exception:"+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 得到已添加了请求签名Header的FinalHttp
     * @return
     */
    private FinalHttp getSignedHttp() {

        String appKey = context.getString(R.string.rongcloud_app_key);
        String appSecret = context.getString(R.string.rongcloud_app_secret);
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
    private String sha1(String value) {
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
