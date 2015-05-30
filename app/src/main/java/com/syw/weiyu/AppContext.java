package com.syw.weiyu;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.LruCache;
import cn.bmob.v3.Bmob;
import com.orhanobut.logger.Logger;
import com.syw.weiyu.api.WeiyuApi;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.dao.user.AccountDao;
import com.syw.weiyu.third.RongCloudEvent;

import com.syw.weiyu.third.LocSDK;
import io.rong.imkit.RongIM;

/**
 * 自有的Application，程序入口，应用程序上下文环境
 * 做一些基本的初始化，保存一些运行时数据
 */
public class AppContext extends Application {
    private static AppContext appContext;
    public static AppContext getCtx() {
        return appContext;
    }
    private static void setAppContext(AppContext appContext) {
        AppContext.appContext = appContext;
    }

    //是否初次启动（Launcher页使用）
    private static boolean isFirstLaunch = true;
    public static boolean isFirstLaunch() {
        return isFirstLaunch;
    }
    public static void setIsFirstLaunch(boolean isFirstLaunch) {
        AppContext.isFirstLaunch = isFirstLaunch;
    }

    /**
     * ===========RAM Cache===========
     */
    public static final String KEY_ACCOUNT = "key_account";
    public static final String KEY_LOCATION = "key_location";
    public static final String KEY_NEARBYSHUOSHUOS = "key_nearbyshuoshuos";
    public static final String KEY_NEARBYUSERS = "key_nearbyusers";
    static final int cacheSize = (int) (Runtime.getRuntime().maxMemory()/2);//最大heap size的一半吧
    static LruCache<String, Object> lruCache = new LruCache<>(cacheSize);
    public static void put(String k,Object v) {
        lruCache.put(k,v);
    }
    public static Object get(String k) {
        return lruCache.get(k);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setAppContext(this);

        initSDKs();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //定位并保存
        WeiyuApi.get().locate();
        //账户数据
        try {
            Account account = new AccountDao().get();
            put(KEY_ACCOUNT,account);
        } catch (AppException e) {
            //do nothing
        }
    }

    /**
     * 初始化SDK组件
     */
    private void initSDKs() {
        // 注册App异常崩溃处理器（防crash）
//        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler(this));

        //初始化日志工具类
        String TAG = "Weiyu";
        Logger.init(TAG);

        //初始化百度云
//        LBSCloud.init(this);
        //初始化定位SDK
        LocSDK.init(this);

        //初始化融云IMKit SDK，should not init RongIM in sub process
        if (isMainThread()) {
            Logger.d("init RongIM");
            RongIM.init(this);
            //初始化融云SDK事件监听处理
            RongCloudEvent.init(this);
        }

        //初始化Bmob
        Bmob.initialize(this, AppConstants.bmob_app_key);
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