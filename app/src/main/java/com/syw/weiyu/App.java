package com.syw.weiyu;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.Logger;
import com.syw.weiyu.third.lbs.LBSCloud;
import com.syw.weiyu.third.im.RongCloudEvent;

import com.syw.weiyu.third.lbs.LocSDK;
import io.rong.imkit.RongIM;

/**
 * 自有的Application，程序入口
 * 做一些基本工具类的初始化
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 初始化日志工具类
         */
        String TAG = "Weiyu";
        Logger.init(TAG);

        /**
         * 初始化百度云
         */
        LBSCloud.init(this);
        /**
         * 初始化定位SDK
         */
        LocSDK.init(this);

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
         * 初始化融云SDK事件监听处理
         */
        RongCloudEvent.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logger.d("onLowMemory,do im disconnect");
        //低內存臨近被清除時，取消鏈接但接收push
        RongIM.getInstance().disconnect(true);
    }

    private String getCurProcessName() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) this
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
        String curProcessName = getCurProcessName();
        return (curProcessName != null && curProcessName.equals(getPackageName()));
    }

}