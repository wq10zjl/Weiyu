package com.syw.weiyu.dao.im;

import android.content.Context;

import com.syw.weiyu.R;

import net.tsz.afinal.FinalHttp;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

/**
 * author: youwei
 * date: 2015-05-12
 * desc:
 */
public class RongCloud {
//    private static FinalHttp signedHttp;
//    private static Context context;
//    private static RongCloud rongCloud;
//    private RongCloud() {}
//    public static RongCloud getInstance(Context ctx) {
//        context = ctx;
//        if (rongCloud == null) {
//            synchronized (RongCloud.class) {
//                if (rongCloud == null) {
//                    rongCloud = new RongCloud();
//                    signedHttp = getSignedHttp();
//                }
//            }
//        }
//        return rongCloud;
//    }

    /**
     * 得到已添加了请求签名Header的FinalHttp
     * @return
     */
    public static FinalHttp getSignedHttp() {

        String appKey = "uwd1c0sxdj831";
        String appSecret = "s3RAbMx2TkaUl";
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
