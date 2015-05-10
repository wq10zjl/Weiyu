package com.syw.weiyu;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.Logger;
import com.syw.weiyu.third.lbs.LBSCloud;
import com.syw.weiyu.third.rongim.RongCloudEvent;

import io.rong.imkit.RongIM;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * init Logger
         */
        String TAG = "Weiyu";
        Logger.init(TAG);

        /**
         * 初始化百度云
         */
        LBSCloud.init(this);

        /**
         * IMKit SDK调用第一步 初始化
         * context上下文
         */
        //should not init RongIM in sub process
        if (isMainThread()) {
            Logger.d("init RongIM");
            RongIM.init(this);
        }

        /**
         * 融云SDK事件监听处理
         */
        RongCloudEvent.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //低內存臨近被清除時，取消鏈接但接收push
        RongIM.getInstance().disconnect(true);
    }

    private String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private boolean isMainThread() {
        String curProcessName = getCurProcessName(this);
        return (curProcessName != null && curProcessName.equals(getPackageName()));
    }

}