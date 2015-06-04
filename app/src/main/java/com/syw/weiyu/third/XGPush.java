package com.syw.weiyu.third;

import com.syw.weiyu.AppConstants;
import com.tencent.xinge.XingeApp;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * author: youwei
 * date: 2015-06-04
 * desc: 信鸽推送
 */
public class XGPush {
    /**
     * 向指定用户ID推送普通消息
     * {"ret_code":0}  //成功
     * {"ret_code":-1, "err_msg":"error description"}  //失败
     注：ret_code为0表示成功，其他为失败，具体请查看附录。
     * @param toUserId
     * @param title
     * @param content
     * @return
     */
    public static boolean pushNotification(String toUserId,String title,String content) {
        JSONObject result = XingeApp.pushAccountAndroid(AppConstants.xgpush_access_id, AppConstants.xgpush_secret_key, title, content, toUserId);
        try {
            return result.getInt("ret_code")==0;
        } catch (JSONException e) {
            return false;
        }
    }

//    public static boolean push
}
