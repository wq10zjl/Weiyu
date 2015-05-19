package com.syw.weiyu.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.orhanobut.logger.Logger;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.AppException;
import com.syw.weiyu.R;
import com.syw.weiyu.api.IAdApi;
import com.syw.weiyu.api.Listener;
import com.syw.weiyu.dao.user.AccountDao;
import com.syw.weiyu.util.IOC;


public class LauncherActivity extends Activity {

    IAdApi adApi = IOC.getAdApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_launcher);

        if (AppContext.getInstance() == null) {
            //未作初始化时，加载开屏广告，并初始化AppContext
            showSplashAdThenGotoMainPage();
            AppContext.init(this);
            AppContext.getInstance().initilize();
        } else {
//          有用户信息→MainTabs主页面
//          无用户信息→Login登入页
            try {
                //已做过初始化，并且有用户信息，直接进入主页面
                new AccountDao().get();
                gotoMainPage();
            } catch (AppException e) {
                //没有账户信息
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

    /**
     * 进入主页
     */
    private void gotoMainPage() {
        //进入主页面
        Intent intent = new Intent(LauncherActivity.this, MainTabsActivity.class);
        startActivity(intent);
        //销毁
        finish();
    }

    private void showSplashAdThenGotoMainPage() {
        //[1s]后加载开屏广告页
        adApi.showSplashAd(this, new Listener() {
            @Override
            public <String> void  onCallback(CallbackType callbackType, String msg) {
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
