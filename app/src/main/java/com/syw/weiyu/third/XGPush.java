package com.syw.weiyu.third;

import android.content.Context;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.core.AppConstants;
import com.syw.weiyu.core.AppException;
import com.syw.weiyu.core.WeiyuApi;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.xinge.Message;
import com.tencent.xinge.XingeApp;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * author: youwei
 * date: 2015-06-04
 * desc: 信鸽推送
 */
public class XGPush {

    /**
     * 注册推送
     * @param context
     */
    public static void register(Context context, String accountId) {
        //注册push
        XGPushManager.registerPush(context,accountId);
    }

    /**
     * 向指定用户ID推送通知
     * @param toUserId
     * @param title
     * @param content
     * @return
     */
    public static boolean pushNotification(String toUserId,String title,String content) {
        JSONObject result = XingeApp.pushAccountAndroid(AppConstants.xgpush_access_id, AppConstants.xgpush_secret_key, title, content, toUserId);
        return isPushed(result);
    }

    /**
     * 推送透传消息
     * @param toUserId
     * @param title
     * @param content
     * @param data json string data
     * @return
     */
    public static boolean pushMessage(String toUserId,String title,String content,String data) {
        Map<String,Object> map = new HashMap<>();
        map.put("data",data);//json string data
        Message message = new Message();
        message.setType(Message.TYPE_MESSAGE);
        message.setTitle(title);
        message.setContent(content);
        message.setCustom(map);
        XingeApp xingeApp = new XingeApp(AppConstants.xgpush_access_id, AppConstants.xgpush_secret_key);
        JSONObject result = xingeApp.pushSingleAccount(0, toUserId, message);
        return isPushed(result);
    }

    /**
     * {"ret_code":0}  //成功
     * {"ret_code":-1, "err_msg":"error description"}  //失败
     * 注：ret_code为0表示成功，其他为失败，具体请查看附录。
     * @param result
     * @return
     */
    private static boolean isPushed(JSONObject result) {
        try {
            return result.getInt("ret_code")==0;
        } catch (JSONException e) {
            return false;
        }
    }
}
