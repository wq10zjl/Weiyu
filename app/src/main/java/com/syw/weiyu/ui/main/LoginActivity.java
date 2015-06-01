package com.syw.weiyu.ui.main;

import android.content.Intent;

import com.syw.weiyu.AppException;
import com.syw.weiyu.api.Listener;
import com.syw.weiyu.api.WeiyuApi;
import com.syw.weiyu.ui.user.LoginBaseActivity;

import com.syw.weiyu.util.Msger;

/**
 * Created by songyouwei on 2015/2/25.
 * 登录填资料的Activity
 */
public class LoginActivity extends LoginBaseActivity {
    @Override
    public void doOnClickWork(String userId, String name, String gender) {
        //注册
        WeiyuApi.get().register(userId, name, gender, new Listener<String>() {
            @Override
            public void onSuccess(String data) {
                //登录
                try {
                    WeiyuApi.get().login(data);
                    showOnSuccessMsg("登录成功");
                    //进入主页面
                    Intent intent = new Intent(LoginActivity.this, MainTabsActivity.class);
                    startActivity(intent);
                    finish();
                } catch (AppException e) {
                    showOnErrorMsg("登录出错");
                    Msger.e(LoginActivity.this, e.getMessage());
                }

            }

            @Override
            public void onFailure(String msg) {
                showOnErrorMsg("登录出错");
                Msger.e(LoginActivity.this, msg);
            }
        });
    }
}
