package com.syw.weiyu.ui.main;

import android.content.Intent;
import android.util.Log;

import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.AppException;
import com.syw.weiyu.api.impl.LocationApi;
import com.syw.weiyu.api.impl.UserApi;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.third.lbs.LBSCloud;
import com.syw.weiyu.third.im.RongCloud;
import com.syw.weiyu.ui.user.ProfileBaseActivity;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.util.ACache;

import net.tsz.afinal.http.AjaxCallBack;

/**
 * Created by songyouwei on 2015/2/25.
 * 登录填资料的Activity
 */
public class LoginActivity extends ProfileBaseActivity {
    @Override
    public void doOnClickWork(String userId, String name, String gender) {

        try {
            new UserApi().register(new User(userId, name, gender, null, null, null));

            Intent intent = new Intent(LoginActivity.this, MainTabsActivity.class);
            startActivity(intent);
            finish();
        } catch (AppException e) {
            showOnErrorMsg(e.getMessage());
        }

//        final User user = new User(userId,name,gender,null,null,null);
        //拿token
//        RongCloud.getInstance(LoginActivity.this).get(userId, name, null, new AjaxCallBack<String>() {
//            @Override
//            public void onSuccess(String s) {
//                Log.w("Weiyu", "Login get return:\n" + s);
//                JSONObject jsonObject = JSON.parseObject(s);
//                if (jsonObject.getString("code").equals("200")) {
//                    //保存token
//                    String token = jsonObject.getString("token");
//                    ACache.getPermanence(LoginActivity.this).put(AppContext.TOKEN, token);
//                    //缓存
//                    AppContext.getInstance().setUser(user);
//                    //保存User
//                    ACache.getPermanence(LoginActivity.this).put(AppContext.USER, user);
//                    //在LBS云创建POI
//                    LBSCloud.getInstance().registerUser(null);
//                    //connect Rong Cloud
//                    RongCloud.getInstance(LoginActivity.this).connectRongCloud();
//                    //跳转
//                    Intent intent = new Intent(LoginActivity.this, MainTabsActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    showOnErrorMsg("请求出错");
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t, int errorNo, String strMsg) {
//                showOnErrorMsg("网络异常");
//                Log.w("Weiyu", "Login get error:\n" + strMsg);
//            }
//        });
    }
}
