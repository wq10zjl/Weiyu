package com.syw.weiyu.ui.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.AppException;
import com.syw.weiyu.api.Listener;
import com.syw.weiyu.api.WeiyuApi;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.third.lbs.LBSCloud;
import com.syw.weiyu.third.im.RongCloud;
import com.syw.weiyu.ui.user.ProfileBaseActivity;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.util.ACache;

import com.syw.weiyu.util.Toaster;
import net.tsz.afinal.http.AjaxCallBack;

/**
 * Created by songyouwei on 2015/2/25.
 * 登录填资料的Activity
 */
public class LoginActivity extends ProfileBaseActivity {
    @Override
    public void doOnClickWork(String userId, String name, String gender) {
        //注册
        WeiyuApi.get().register(userId, name, gender, new Listener<String>() {
            @Override
            public void onCallback(@NonNull CallbackType callbackType, @Nullable String data, @Nullable String msg) {
                if (callbackType == CallbackType.onSuccess) {
                    try {
                        //登录
                        WeiyuApi.get().login(data);
                        //进入主页面
                        Intent intent = new Intent(LoginActivity.this, MainTabsActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (AppException e) {
                        Toaster.e(LoginActivity.this, e.getMessage());
                    }
                } else {
                    Toaster.e(LoginActivity.this,msg);
                }
            }
        });
    }
}
