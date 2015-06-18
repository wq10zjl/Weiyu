package com.syw.weiyu.util;

import android.app.Activity;
import com.devspark.appmsg.AppMsg;

/**
 * author: songyouwei
 * date: 2015-05-21
 * desc: 消息提示，代替toast
 */
public class Msger {
    private static final int duration = 2000;

    public static void iStickey(Activity ctx,String msg) {
        AppMsg.cancelAll(ctx);
        // create {@link AppMsg} with specify type
        AppMsg appMsg = AppMsg.makeText(ctx, msg, AppMsg.STYLE_INFO);
        appMsg.setDuration(AppMsg.LENGTH_STICKY);
        appMsg.show();
    }
    public static void i(Activity ctx,String msg) {
        AppMsg.cancelAll(ctx);
        // create {@link AppMsg} with specify type
        AppMsg appMsg = AppMsg.makeText(ctx, msg, AppMsg.STYLE_INFO);
        appMsg.setDuration(duration);
        appMsg.makeText(ctx,msg,AppMsg.STYLE_INFO).show();
    }
    public static void e(Activity ctx,String msg) {
        AppMsg.cancelAll(ctx);
        // create {@link AppMsg} with specify type
        AppMsg appMsg = AppMsg.makeText(ctx, msg, AppMsg.STYLE_INFO);
        appMsg.setDuration(duration);
        appMsg.makeText(ctx,msg,AppMsg.STYLE_ALERT).show();
    }
}
