package com.syw.weiyu.util;

import android.app.Activity;
import com.devspark.appmsg.AppMsg;
import com.syw.weiyu.R;
import com.syw.weiyu.core.App;

/**
 * author: songyouwei
 * date: 2015-05-21
 * desc: 消息提示，代替toast
 */
public class Msger {
    private static final int duration = 1500;
    private static final AppMsg.Style error = new AppMsg.Style(duration, R.color.red);
    private static final AppMsg.Style info = new AppMsg.Style(duration, R.color.green);
    private static final AppMsg.Style stickeyInfo = new AppMsg.Style(AppMsg.LENGTH_STICKY, R.color.green);

    public static void iStickey(Activity ctx,String msg) {
        AppMsg.cancelAll(ctx);
        // create {@link AppMsg} with specify type
        AppMsg.makeText(ctx, msg, stickeyInfo).show();
    }

    public static void i(Activity ctx,String msg) {
        AppMsg.cancelAll(ctx);
        // create {@link AppMsg} with specify type
        AppMsg.makeText(ctx, msg, info).show();
    }
    public static void e(Activity ctx,String msg) {
        AppMsg.cancelAll(ctx);
        // create {@link AppMsg} with specify type
        AppMsg.makeText(ctx, msg, error).show();
    }
}
