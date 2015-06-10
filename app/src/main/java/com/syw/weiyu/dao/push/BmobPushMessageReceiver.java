package com.syw.weiyu.dao.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import cn.bmob.push.PushConstants;
import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.syw.weiyu.R;
import com.syw.weiyu.bean.Comment;
import com.syw.weiyu.core.App;
import com.syw.weiyu.ui.session.CommentMessageActivity;
import net.tsz.afinal.FinalDb;

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
            //parse comment
            Comment comment = JSON.parseObject(content, Comment.class);
            FinalDb finalDb = FinalDb.create(context);
            finalDb.save(comment);

            Intent resultIntent = new Intent(context, CommentMessageActivity.class);
//            resultIntent.putExtra("comment", comment);
            pushLocalNotify(context, "微遇", "你收到了一条新的评论", intent);
        }
    }

    private static void pushLocalNotify(Context context, String title, String text, Intent intent) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(text);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(App.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

}