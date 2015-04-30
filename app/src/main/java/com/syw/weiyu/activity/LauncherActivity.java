package com.syw.weiyu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
//import android.widget.FrameLayout;
//
//import com.baidu.mobads.SplashAd;
//import com.baidu.mobads.SplashAdListener;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.R;
import com.syw.weiyu.RongIM.RongCloud;
import com.syw.weiyu.ad.MoGo;
import com.syw.weiyu.splash.WeiyuSplashListener;

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
        //否则，初始化AppContext，并加载开屏广告
            /**
             * 初始化AppContext，读取并缓存运行时数据
             */
            AppContext.init(this);
            Log.d("Weiyu","AppContext.init(this)");

            //[1s]后加载腾讯开屏广告页
//            new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    initAndShowGDTSplashAd();
//                }
//            }.sendEmptyMessageDelayed(1,1000);

            //[1s]后加载百度开屏广告页
//            new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    initAndShowBaiduSplashAd();
//                }
//            }.sendEmptyMessageDelayed(1,1000);

            showSplashAd();

        }

    }

    private void showSplashAd() {
        //新用户就不加载开屏广告了
        if (AppContext.getInstance().getUser() == null) {
            mainPageHandler.sendEmptyMessageDelayed(1,2000);
            return;
        }
        //[1s]后加载开屏广告页
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                new MoGo(LauncherActivity.this).showSplashAd(new WeiyuSplashListener() {
                    @Override
                    public void onSplashClickAd(String s) {

                    }

                    @Override
                    public void onSplashRealClickAd(String s) {

                    }

                    @Override
                    public void onSplashError(String s) {
//                onSplashClose();
                    }

                    @Override
                    public void onSplashSucceed() {

                    }

                    @Override
                    public void onSplashClose() {
                        mainPageHandler.sendEmptyMessage(1);
                    }
                });
            }
        }.sendEmptyMessageDelayed(1,1000);
    }

    /**
     * 初始化并显示百度联盟开屏广告(实时开屏广告)
     */
//    private void initAndShowBaiduSplashAd() {
//        //准备展示开屏广告的容器
//        FrameLayout container = (FrameLayout) this
//                .findViewById(R.id.splashcontainer);
//        SplashAdListener listener=new SplashAdListener() {
//            @Override
//            public void onAdDismissed() {
//                Log.i("Weiyu", "RSplashAd onAdDismissed");
//                jumpWhenCanClick();// 跳转至您的应用主界面
//            }
//
//            @Override
//            public void onAdFailed(String arg0) {
//                Log.i("Weiyu", "RSplashAd onAdFailed："+arg0);
//                mainPageHandler.sendEmptyMessage(1);
//            }
//
//            @Override
//            public void onAdPresent() {
//                Log.i("Weiyu", "RSplashAd onAdPresent");
//            }
//
//            @Override
//            public void onAdClick() {
//                Log.i("Weiyu", "RSplashAd onAdClick");
//                //设置开屏可接受点击时，该回调可用
//            }
//        };
//        /**
//         * 默认开屏构造函数：
//         * new SplashAd(Context context, ViewGroup adsParent,
//         * 				SplashAdListener listener, SplashType splashType);
//         * 实时开屏默认接受点击，使用样例中的jumpWhenCanClick方法来跳转；
//         */
////		new SplashAd(this, adsParent, listener, SplashType.REAL_TIME);
//        /**
//         * 实时开屏默认接受点击。如果想让开屏不接受点击，使用以下构造函数：
//         * new SplashAd(Context context, ViewGroup adsParent,
//         * 				SplashAdListener listener,String posId, boolean canClick, SplashType splashType);
//         * 因当前posId（广告位ID）需设置为空，故可使用如下代码进行创建：
//         */
//        new SplashAd(this, container, listener, "", true, SplashAd.SplashType.REAL_TIME);
//    }
//    /**
//     * 当设置开屏可点击时，需要等待跳转页面关闭后，再切换至您的主窗口。故此时需要增加waitingOnRestart判断。
//     * 另外，点击开屏还需要在onRestart中调用jumpWhenCanClick接口。
//     */
//    public boolean waitingOnRestart=false;
//    private void jumpWhenCanClick() {
//        Log.d("test", "this.hasWindowFocus():"+this.hasWindowFocus());
//        if(this.hasWindowFocus()||waitingOnRestart){
//            mainPageHandler.sendEmptyMessage(1);
//        }else{
//            waitingOnRestart=true;
//        }
//
//    }

    /**
     * 初始化并显示腾讯广点通开屏广告(只有实时开屏广告)
     */
//    private void initAndShowGDTSplashAd() {
//        //准备展示开屏广告的容器
//        FrameLayout container = (FrameLayout) this
//                .findViewById(R.id.splashcontainer);
////创建开屏广告，广告拉取成功后会自动展示在container中。Container会首先被清空
//        new SplashAd(this, container, getString(R.string.gdt_app_id), getString(R.string.gdt_adid_splash),
//                new SplashAdListener() {
//                    //广告拉取成功开始展示时调用
//                    public void onAdPresent() {
//                        Log.d("Weiyu","Splash onAdPresent");
//                    }
//                    //广告拉取超时（3s）或者没有广告时调用，errCode参见SplashAd类的常量声明
//                    public void onAdFailed(int errCode) {
//                        String errMsg = "";
//                        switch (errCode) {
//                            case SplashAd.ERROR_CONTAINER_INVISIBLE:errMsg = "ERROR_CONTAINER_INVISIBLE";break;
//                            case SplashAd.ERROR_LOAD_AD_FAILED:errMsg = "ERROR_LOAD_AD_FAILED";break;
//                            case SplashAd.ERROR_REJECT_LOAD_AD:errMsg = "ERROR_REJECT_LOAD_AD";break;
//                            case SplashAd.INTERNAL_ERROR:errMsg = "INTERNAL_ERROR";break;
//                            default:errMsg = "UNKNOWN";
//                        }
//                        Log.d("Weiyu","Splash onAdFailed, errCode:"+errCode+" errorMsg:"+errMsg);
//                        mainPageHandler.sendEmptyMessage(1);
//                    }
//                    //广告展示时间结束（5s）或者用户点击关闭时调用。
//                    public void onAdDismissed() {
//                        Log.d("Weiyu","Splash onAdDismissed");
//                        mainPageHandler.sendEmptyMessage(1);
//                    }
//                });
//    }

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
