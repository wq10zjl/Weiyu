package com.syw.weiyu.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import com.orhanobut.logger.Logger;

/**
 * author: youwei
 * date: 2015-05-12
 * desc:
 */
public class AppException extends Exception implements Thread.UncaughtExceptionHandler {
    public Type type;
    public AppException(Type type,String detailMessage) {
        super(detailMessage);
        this.type = type;
    }
    public AppException(String detailMessage) {
        super(detailMessage);
    }

    public enum Type {
        network,io,json,other
    }

    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private App mContext;
    private AppException(Context context) {
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.mContext = (App) context;
    }
    /**
     * 获取APP异常崩溃处理对象
     *
     * @param context
     * @return
     */
    public static AppException getAppExceptionHandler(Context context) {
        return new AppException(context.getApplicationContext());
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }
    /**
     * 自定义异常处理:收集错误信息&发送错误报告
     *
     * @param ex
     * @return true:处理了该异常信息;否则返回false
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        if (mContext == null) {
            return false;
        }
//        final Context context = AppManager.getAppManager().currentActivity();
//        final String crashReport = getCrashReport(context, ex);
//        //显示异常信息&发送报告
//        new Thread() {
//            public void run() {
//                Looper.prepare();
//                UIHelper.sendAppCrashReport(context, crashReport);
//                Looper.loop();
//            }
//
//        }.start();
        Logger.e(getCrashReport(mContext,ex));
        return true;
    }
    /**
     * 获取APP崩溃异常报告
     *
     * @param ex
     * @return
     */
    private String getCrashReport(Context context, Throwable ex) {
        PackageInfo pinfo = ((App) context.getApplicationContext()).getPackageInfo();
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("Version: " + pinfo.versionName + " (" + pinfo.versionCode + ")\n");
        exceptionStr.append("API Level: " + android.os.Build.VERSION.SDK_INT + "\n");
        exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE + " (" + android.os.Build.MODEL + ")\n\n\n");
        exceptionStr.append("异常信息: \n");
        exceptionStr.append("Exception: " + ex.getMessage() + "\n\n\n");
        exceptionStr.append("堆栈信息: \n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionStr.append(elements[i].toString() + "\n");
        }
        return exceptionStr.toString();
    }
}
