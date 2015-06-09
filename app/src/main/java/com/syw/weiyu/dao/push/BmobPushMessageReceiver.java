package com.syw.weiyu.dao.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.bmob.push.PushConstants;
import com.orhanobut.logger.Logger;

/**
 * author: songyouwei
 * date: 2015-06-10
 * desc:
 */
public class BmobPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String content = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            Logger.d("bmob客户端收到推送内容：" + content);
        }
    }

}