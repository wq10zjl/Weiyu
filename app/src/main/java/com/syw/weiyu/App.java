package com.syw.weiyu;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.syw.weiyu.LBS.LBSCloud;
import com.syw.weiyu.RongIM.RongCloudEvent;

import io.rong.imkit.RongIM;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 初始化百度云
         */
        LBSCloud.init(this);

        /**
         * init Wandoujia Ad SDK
         */
//        try {
//            Ads.init(this, getString(R.string.wdj_ads_app_id), getString(R.string.wdj_ads_app_secret_key));
//            //preload the ads
//            Ads.preLoad(this, Fetcher.AdFormat.appwall, "GAME", getString(R.string.wdj_adid_appwall));
//        } catch (Exception e) {
//            Log.w("Weiyu"," Wandoujia ADs SDK init exception");
//            e.printStackTrace();
//        }

        /**
         * IMKit SDK调用第一步 初始化
         * context上下文
         */
        //should not init RongIM in sub process
        if (isMainThread()) {
            RongIM.init(this);
            Log.i("Weiyu", "init RongIM");
        }
        /**
         * 融云SDK事件监听处理
         */
        RongCloudEvent.init(this);
    }

    private static String getCurProcessName(Context context) {
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
        return getCurProcessName(this).equals(getPackageName());
    }
}