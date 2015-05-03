package com.syw.weiyu.ad;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.syw.weiyu.R;
import com.syw.weiyu.activity.MainTabsActivity;
import com.syw.weiyu.av.WeiyuLayout;
import com.syw.weiyu.av.WeiyuWebView;
import com.syw.weiyu.controller.listener.WeiyuListener;
import com.syw.weiyu.splash.WeiyuSplash;
import com.syw.weiyu.splash.WeiyuSplashListener;
import com.syw.weiyu.util.WeiyuSize;
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

    void isShown() {
    }

    public View getBannerAd(WeiyuListener listener) {
        /**
         * 初始化adsMogoView
         * 参数：第一个activity,第二个mogoID（该值为芒果后台申请的生产的芒果ID，非单一平台ID）,第三个设置广告展示位置,第四个请求广告尺寸,
         * 第五个是否手动刷新true：是手动刷新（芒果后台轮换时间必须为禁用才会生效）	，false:自动轮换
         */
        weiyuLayoutCode = new WeiyuLayout(activity, activity.getString(R.string.adsmogo_appid), WeiyuSize.WeiyuAutomaticScreen);

        weiyuLayoutCode.setWeiyuListener(listener);
        weiyuLayoutCode.setCloseButtonVisibility(View.VISIBLE);
        weiyuLayoutCode.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        weiyuLayoutCode.downloadIsShowDialog = true;

        return weiyuLayoutCode;
    }

    public View getBannerAd() {
        return getBannerAd(null);
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
