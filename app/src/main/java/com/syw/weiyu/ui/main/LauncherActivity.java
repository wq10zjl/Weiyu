package com.syw.weiyu.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.orhanobut.logger.Logger;
import com.syw.weiyu.AppException;
import com.syw.weiyu.R;
import com.syw.weiyu.api.Listener;
import com.syw.weiyu.api.WeiyuApi;


public class LauncherActivity extends Activity {

    //是否初次启动
    private boolean isFirstLaunch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_launcher);

        if (isFirstLaunch) {
            //未作初始化时，加载开屏广告
            showSplashAdThenGotoMainPage();
            isFirstLaunch = false;
        } else {
//          有用户信息→MainTabs主页面
//          无用户信息→Login登入页
            try {
                //有账户信息可直接进入主页面，否则会抛出异常
                WeiyuApi.get().login();
                gotoMainPage();
            } catch (AppException e) {
                //没有账户信息或token为空，进入登录页
                gotoLoginPage();
            }
        }
    }

    private void gotoLoginPage() {
        //进入注册页
        Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
        startActivity(intent);
        //销毁
        finish();
    }

    private void gotoMainPage() {
        //进入主页面
        Intent intent = new Intent(LauncherActivity.this, MainTabsActivity.class);
        startActivity(intent);
        //销毁
        finish();
    }

    private void showSplashAdThenGotoMainPage() {
        //[1s]后加载开屏广告页
        WeiyuApi.get().showSplashAd(this, new Listener<String>() {
            @Override
            public void onCallback(CallbackType callbackType, String msg) {
                switch (callbackType) {
                    case onAdError:
                        Logger.d(msg.toString());
                        gotoMainPage();
                        break;
                    case onAdClick:
                        Logger.d(msg.toString());
                        break;
                    case onAdClose:
                    default:
                        gotoMainPage();
                        break;
                }
            }
        });
    }


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
