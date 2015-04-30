package com.syw.weiyu.ad;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.syw.weiyu.R;
import com.syw.weiyu.activity.MainTabsActivity;
import com.syw.weiyu.av.WeiyuLayout;
import com.syw.weiyu.splash.WeiyuSplash;
import com.syw.weiyu.splash.WeiyuSplashListener;
import com.syw.weiyu.util.WeiyuSplashMode;

/**
 * Created by songyouwei on 2015/4/28.
 * AdMoGo平台
 */
public class MoGo {
    private Activity activity;
    public MoGo (Activity act) {
        activity = act;
    }
    //adsmogo
    WeiyuLayout weiyuLayoutCode;

    public void showBannerAd() {
        /**
         * step1,初始化adsMogoView
         * 参数：第一个activity,第二个mogoID（该值为芒果后台申请的生产的芒果ID，非单一平台ID）,第三个设置广告展示位置,第四个请求广告尺寸,
         * 第五个是否手动刷新true：是手动刷新（芒果后台轮换时间必须为禁用才会生效）	，false:自动轮换
         */
        weiyuLayoutCode = new WeiyuLayout(activity, activity.getString(R.string.adsmogo_appid));
        /**
         * step2,设置监听
         * 参数：为AdsMogoListener
         */
        weiyuLayoutCode.setWeiyuListener(null);
        weiyuLayoutCode.setCloseButtonVisibility(View.VISIBLE);
        /**
         * step3,添加adsMogoView到指定的view中
         */
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        // 设置广告出现的位置(悬浮于底部)
        params.bottomMargin = 0;
        params.gravity = Gravity.BOTTOM;
        params.leftMargin=0;
        params.rightMargin=0;
        activity.addContentView(weiyuLayoutCode, params);
        /*********************** 代码添加广告结束 ************************/
    }

    /**
     * 在Activity的onDestroy方法下调用
     */
    public void onBannerDestory() {
        WeiyuLayout.clear();
        weiyuLayoutCode.clearThread();
    }

    public void showSplashAd(WeiyuSplashListener listener) {
        WeiyuSplash weiyuSplash = new WeiyuSplash(activity,activity.getString(R.string.adsmogo_appid), WeiyuSplashMode.FULLSCREEN);
        //设置开屏广告监听
        weiyuSplash.setWeiyuSplashListener(listener);
    }
}
