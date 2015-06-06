package com.syw.weiyu.dao.push;

import android.content.Context;
import com.tencent.android.tpush.*;
import org.json.JSONException;
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
        if (context!=null && xgPushTextMessage!=null) {
            try {
                JSONObject object = new JSONObject(xgPushTextMessage.getCustomContent());
                if (!object.isNull("data")) {
                    String data = object.getString("data");//json string data
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

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
