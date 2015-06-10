package com.syw.weiyu.dao.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import cn.bmob.push.PushConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;
import com.syw.weiyu.R;
import com.syw.weiyu.bean.Comment;
import com.syw.weiyu.core.App;
import com.syw.weiyu.ui.LauncherActivity;
import com.syw.weiyu.ui.session.CommentMessageActivity;
import com.syw.weiyu.util.StringUtil;
import net.tsz.afinal.FinalDb;

/**
 * author: songyouwei
 * date: 2015-06-10
 * desc:
 */
public class BmobPushMessageReceiver extends BroadcastReceiver {

    /**
     * 消息格式：
     * {
     * "type":"comment"/"normal",
     * "data":"{json obj}"/"string"
     * }
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String content = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            Logger.d("bmob客户端收到推送内容：" + content);
            JSONObject jsonObject = JSON.parseObject(content);
            if (!StringUtil.isEmpty(jsonObject.getString("alert"))) {
                pushLocalNotify(context,"微遇",jsonObject.getString("alert"),new Intent(context, LauncherActivity.class));
            } else {
                //comment
                Comment comment = JSON.parseObject(content,Comment.class);
                FinalDb finalDb = FinalDb.create(context);
                finalDb.save(comment);

                Intent resultIntent = new Intent(context, CommentMessageActivity.class);
                pushLocalNotify(context, "微遇", comment.getUserName()+"评论了你的说说", resultIntent);
            }
        }
    }

    private static void pushLocalNotify(Context context, String title, String text, Intent intent) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
//                .setVibrate(new long[]{1000, 1000})
                .setLights(Color.BLUE, 3000, 3000)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                ;
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.add
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        Notification notification = mBuilder.build();
        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(App.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, notification);
    }

}