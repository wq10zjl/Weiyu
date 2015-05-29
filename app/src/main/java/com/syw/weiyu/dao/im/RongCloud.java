package com.syw.weiyu.dao.im;

import android.content.Context;

import android.support.annotation.NonNull;
import com.orhanobut.logger.Logger;
import com.syw.weiyu.AppConstants;
import com.syw.weiyu.AppException;
import com.syw.weiyu.R;

import com.syw.weiyu.third.im.RongCloudEvent;
import com.syw.weiyu.util.StringUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import net.tsz.afinal.FinalHttp;

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
                public void onSuccess(String s) {
                    // 此处处理连接成功。
                    Logger.d("Connect rongcloud success.");
                    //设置其它的监听器
                    RongCloudEvent.getInstance().setOtherListener();
                }

                @Override
                public void onError(ErrorCode errorCode) {
                    // 此处处理连接错误。
                    Logger.e("Connect rongcloud failed, errormsg:" + errorCode.getMessage());
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
