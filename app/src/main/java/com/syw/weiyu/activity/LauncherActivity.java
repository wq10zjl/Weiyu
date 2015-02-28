package com.syw.weiyu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.qq.e.splash.SplashAd;
import com.qq.e.splash.SplashAdListener;
import com.qq.e.v2.constants.Constants;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.R;
import com.syw.weiyu.RongIM.RongCloud;

public class LauncherActivity extends Activity {

    private static final String TAG = "LAUNCHER_ACTIVITY";

    private Handler mainPageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //若存在用户信息，则直接进入主页面
            if (AppContext.getInstance().getUser() != null) {
                //连接融云IM
                RongCloud.getInstance(LauncherActivity.this).connectRongCloud();
                //进入主页面
                Intent intent = new Intent(LauncherActivity.this, MainTabsActivity.class);
                startActivity(intent);
            } else {
                //否则进入注册页
                Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            //销毁
            LauncherActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_launcher);

        //若已做过初始化，直接连接RongCloud并进入主页面
        if (AppContext.getInstance() != null) {
            mainPageHandler.sendEmptyMessage(1);
        } else {
            /**
             * 初始化AppContext，读取并缓存运行时数据
             */
            AppContext.init(this);
            Log.d("Weiyu","AppContext.init(this)");

            //2s后进入主页
//            mainPageHandler.sendEmptyMessageDelayed(1, 2000);

            //1.5s后再显示多盟开屏广告，广告时间3s
//            initAndShowDomobSplashAd(1500,3000);

            //[1s]后加载广告开屏页
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    initAndShowGDTSplashAd();
                }
            }.sendEmptyMessageDelayed(1,1000);

        }

    }

    /**
     * 初始化并显示腾讯广点通开屏广告(只有实时开屏广告)
     */
    private void initAndShowGDTSplashAd() {
        //准备展示开屏广告的容器
        FrameLayout container = (FrameLayout) this
                .findViewById(R.id.splashcontainer);
//创建开屏广告，广告拉取成功后会自动展示在container中。Container会首先被清空
        new SplashAd(this, container, getString(R.string.gdt_app_id), getString(R.string.gdt_adid_splash),
                new SplashAdListener() {
                    //广告拉取成功开始展示时调用
                    public void onAdPresent() {
                        Log.d("Weiyu","Splash onAdPresent");
                    }
                    //广告拉取超时（3s）或者没有广告时调用，errCode参见SplashAd类的常量声明
                    public void onAdFailed(int errCode) {
                        String errMsg = "";
                        switch (errCode) {
                            case SplashAd.ERROR_CONTAINER_INVISIBLE:errMsg = "ERROR_CONTAINER_INVISIBLE";break;
                            case SplashAd.ERROR_LOAD_AD_FAILED:errMsg = "ERROR_LOAD_AD_FAILED";break;
                            case SplashAd.ERROR_REJECT_LOAD_AD:errMsg = "ERROR_REJECT_LOAD_AD";break;
                            case SplashAd.INTERNAL_ERROR:errMsg = "INTERNAL_ERROR";break;
                            default:errMsg = "UNKNOWN";
                        }
                        Log.d("Weiyu","Splash onAdFailed, errCode:"+errCode+" errorMsg:"+errMsg);
                        mainPageHandler.sendEmptyMessage(1);
                    }
                    //广告展示时间结束（5s）或者用户点击关闭时调用。
                    public void onAdDismissed() {
                        Log.d("Weiyu","Splash onAdDismissed");
                        mainPageHandler.sendEmptyMessage(1);
                    }
                });
    }

//    /**
//     * 初始化并显示多盟开屏广告(实时开屏广告)
//     * @param originalSplashTime 显示原始开屏页的时间
//     * @param adSplashTime 开屏广告页的显示时间
//     */
//    private void initAndShowDomobSplashAd(long originalSplashTime, final long adSplashTime) {
//        final RTSplashAd splashAd
//                = new RTSplashAd(
//                this,
//                this.getString(R.string.domob_publisher_id),
//                this.getString(R.string.domob_adid_splash),
//                cn.domob.android.ads.SplashAd.SplashMode.SplashModeFullScreen
//                );
//        splashAd.setRTSplashAdListener(new RTSplashAdListener() {
//            @Override
//            public void onRTSplashPresent() {
//                Log.d("Weiyu", "onRTSplashPresent");
//                mainPageHandler.sendEmptyMessageDelayed(1, adSplashTime);
//            }
//
//            @Override
//            public void onRTSplashDismiss() {
//                Log.i("Weiyu", "onRTSplashDismiss");
//                mainPageHandler.sendEmptyMessage(1);
//            }
//
//            @Override
//            public void onRTSplashLoadFailed() {
//                Log.d("Weiyu", "onRTSplashLoadFailed");
//            }
//        });
//
//        //显示广告
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                splashAd.splash(LauncherActivity.this, LauncherActivity.this.findViewById(R.id.splashcontainer));
//            }
//        },originalSplashTime);
//    }

    //禁用返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
