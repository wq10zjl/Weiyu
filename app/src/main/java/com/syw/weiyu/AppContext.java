package com.syw.weiyu;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.orhanobut.logger.Logger;
import com.syw.weiyu.third.lbs.LBSCloud;
import com.syw.weiyu.third.im.RongCloudEvent;

import com.syw.weiyu.third.lbs.LocSDK;
import io.rong.imkit.RongIM;

/**
 * 自有的Application，程序入口，应用程序上下文环境
 * 做一些基本的初始化
 */
public class AppContext extends Application {

    private static AppContext appContext;

    public static AppContext getAppContext() {
        return appContext;
    }
    private static void setAppContext(AppContext appContext) {
        AppContext.appContext = appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setAppContext(this);

        // 注册App异常崩溃处理器（防crash）
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler(this));

        //初始化日志工具类
        String TAG = "Weiyu";
        Logger.init(TAG);

        //初始化百度云
        LBSCloud.init(this);
        //初始化定位SDK
        LocSDK.init(this);

        //初始化融云IMKit SDK，should not init RongIM in sub process
        if (isMainThread()) {
            Logger.d("init RongIM");
            RongIM.init(this);
            //初始化融云SDK事件监听处理
            RongCloudEvent.init(this);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logger.d("onLowMemory,do im disconnect");
        //低內存臨近被清除時，取消鏈接但接收push
        RongIM.getInstance().disconnect(true);
    }

    /**
     * 获取App安装包信息
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
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