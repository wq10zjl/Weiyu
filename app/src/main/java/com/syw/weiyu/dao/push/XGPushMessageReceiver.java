package com.syw.weiyu.dao.push;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.tencent.android.tpush.*;
import org.json.JSONObject;

/**
 * author: youwei
 * date: 2015-06-05
 * desc:
 */
public class XGPushMessageReceiver extends XGPushBaseReceiver {

    /**
     * 透传消息接收处理
     */
    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
//        if (context!=null && xgPushTextMessage!=null) {
//            try {
//                JSONObject object = new JSONObject(xgPushTextMessage.getCustomContent());
//                if (!object.isNull("data")) {
//                    String data = object.getString("data");//json string data
//                    //parse data
//                    XGLocalMessage localMessage = new XGLocalMessage();
//                    localMessage.set
//                    localMessage.setTitle(xgPushTextMessage.getTitle());
//                    localMessage.setContent(xgPushTextMessage.getContent());
//                    localMessage.setActivity(CommentMessageActivity.class.getName());
//                    XGPushManager.addLocalNotification(context, localMessage);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

    }

    /**
     * 通知被展示触发的结果，可以在此保存APP收到的通知
     */
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        if (context!=null && xgPushShowedResult!=null) {
            try {
                JSONObject object = new JSONObject(xgPushShowedResult.getCustomContent());
                String data = object.getString("data");//json string data
                Class clazz = Class.forName(object.getString("clazz"));
                JSON.parseObject(data,clazz);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e(e.getMessage());
            }
        }
    }


    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }
}
