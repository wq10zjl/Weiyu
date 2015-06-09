package com.syw.weiyu.dao.push;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.bean.Comment;
import com.syw.weiyu.core.App;
import com.syw.weiyu.core.AppConstants;
import com.syw.weiyu.ui.explore.CommentMessageActivity;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.Style;
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
     * 反注册推送
     */
    public static void unregister(Context context) {
        //注册push
        XGPushManager.unregisterPush(context);
    }

    public static void pushNewComment(Account account, String toUserId,Comment comment) {
        pushNotification(account,toUserId, App.getCtx().getApplicationInfo().name, account.getName()+"评论了你的说说", JSON.toJSONString(comment), Comment.class);
    }

    /**
     * 向指定用户ID推送通知
     * @param account
     * @param toUserId
     * @param title
     * @param content
     * @return
     */
    private static boolean pushNotification(Account account, String toUserId,String title,String content, String jsonData, Class clazz) {
        Map<String,Object> customData = new HashMap<>();
        customData.put("data", jsonData);//json string data
        customData.put("clazz", clazz.getName());
        Style style = new Style(0,1,0,1,-1,1,0,1);//依次为$builderId[,$ring][,$vibrate][,$clearable][,$nId][,$lights][,$iconType][,$styleId]
        style.setIconRes("ic_launcher.png");
        ClickAction clickAction = new ClickAction();
        clickAction.setActivity(CommentMessageActivity.class.getName());
        Message message = new Message();
        message.setType(Message.TYPE_NOTIFICATION);
        message.setAction(clickAction);
        message.setTitle(title);
        message.setContent(content);
        message.setCustom(customData);
        XingeApp xingeApp = new XingeApp(AppConstants.xgpush_access_id, AppConstants.xgpush_secret_key);
        JSONObject result = xingeApp.pushSingleAccount(0, toUserId, message);
        return isPushed(result);

//        JSONObject result = XingeApp.pushAccountAndroid(AppConstants.xgpush_access_id, AppConstants.xgpush_secret_key, title, content, toUserId);
//        return isPushed(result);
    }

    /**
     * 推送透传消息
     * @param toUserId
     * @param title
     * @param content
     * @param jsonData json string data
     *        {type:hello/comment/,fromUserId:xx,toUserId:xx,}
     * @return
     */
    @Deprecated
    private static boolean pushMessage(String toUserId,String title,String content,String jsonData) {
//        Map<String,Object> map = new HashMap<>();
//        map.put("data",jsonData);//json string data
//        Message message = new Message();
//        message.setType(Message.TYPE_MESSAGE);
//        message.setTitle(title);
//        message.setContent(content);
//        message.setCustom(map);
//        XingeApp xingeApp = new XingeApp(AppConstants.xgpush_access_id, AppConstants.xgpush_secret_key);
//        JSONObject result = xingeApp.pushSingleAccount(0, toUserId, message);
//        return isPushed(result);
        return false;
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
