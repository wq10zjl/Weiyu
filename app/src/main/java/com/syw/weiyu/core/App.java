package com.syw.weiyu.core;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.LruCache;
import cn.bmob.v3.Bmob;
import com.orhanobut.logger.Logger;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.dao.push.BmobPushHelper;
import com.syw.weiyu.dao.user.LocalAccountDao;
import com.syw.weiyu.dao.im.RongCloudEvent;

import com.syw.weiyu.dao.location.LocSDK;
import io.rong.imkit.RongIM;
import net.tsz.afinal.FinalDb;

/**
 * 自有的Application，程序入口，应用程序上下文环境
 * 做一些基本的初始化，保存一些运行时数据
 */
public class App extends Application {
    private static App app;

    public static App getCtx() {
        return app;
    }

    private static void setApp(App app) {
        App.app = app;
    }

    //是否初次启动（Launcher页使用）
    private static boolean isFirstLaunch = true;

    public static boolean isFirstLaunch() {
        return isFirstLaunch;
    }

    public static void setIsFirstLaunch(boolean isFirstLaunch) {
        App.isFirstLaunch = isFirstLaunch;
    }

    /**
     * ===========RAM Cache===========
     */
    public static final String KEY_ACCOUNT = "key_account";
    public static final String KEY_LOCATION = "key_location";
    public static final String KEY_NEARBYSHUOSHUOS = "key_nearbyshuoshuos";
    public static final String KEY_NEARBYUSERS = "key_nearbyusers";
    static final int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 2);//最大heap size的一半吧
    static LruCache<String, Object> lruCache = new LruCache<>(cacheSize);

    public static void putCache(String k, Object v) {
        lruCache.put(k, v);
    }

    public static Object getCache(String k) {
        return lruCache.get(k);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setApp(this);

        // 注册App异常崩溃处理器（防crash）
//        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler(this));

        try {
            //初始化日志工具类
            String TAG = "Weiyu";
            Logger.init(TAG);

//            if (isMainThread()) {
                //初始化融云IMKit SDK，should not init RongIM in sub process
                RongIM.init(this);
                //初始化融云SDK事件监听处理
                RongCloudEvent.init(this);
//            }

            //初始化定位SDK
            LocSDK.init(this);
            //定位并保存
            WeiyuApi.get().locate();

            //初始化BmobSDK
            Bmob.initialize(this, AppConstants.bmob_app_id);

            //拿账户数据，登录IM&推送
            Account account = new LocalAccountDao().get();
            /*======有账户数据时会执行======*/
            doThingsWithAccount(account);

        } catch (Exception e) {
//            clearAppData();
            Logger.e(e.getMessage());
        }
    }

    /**
     * 在有账户的时候要做的一些初始化操作
     * 用户登录成功后要补充
     * @param account
     */
    public void doThingsWithAccount(@NonNull final Account account) {
        putCache(KEY_ACCOUNT, account);
        // 使用推送服务
        BmobPushHelper.initAndStartPushClient(this, account);
        //login IM
        try { WeiyuApi.get().login(account.getToken()); } catch (AppException e) {}
    }


    private void clearAppData() {
        FinalDb.create(this).dropDb();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logger.d("onLowMemory,do im logout");
        //低內存臨近被清除時，取消鏈接但接收push
        RongIM.getInstance().disconnect();
    }

    /**
     * 获取App安装包信息
     *
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