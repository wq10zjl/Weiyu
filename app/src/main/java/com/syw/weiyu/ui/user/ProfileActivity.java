package com.syw.weiyu.ui.user;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.dd.processbutton.iml.ActionProcessButton;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.third.lbs.LBSCloud;
import com.syw.weiyu.R;
import com.syw.weiyu.third.im.RongCloud;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.util.ACache;

import net.tsz.afinal.http.AjaxCallBack;

/**
 * Created by songyouwei on 2015/2/25.
 * 修改资料的Activity
 */
public class ProfileActivity extends ProfileBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set back icon
        ImageView imageView = (ImageView) findViewById(R.id.header_iv_logo);
        imageView.setImageResource(R.drawable.wy_icon_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //set progress button
        ActionProcessButton button = (ActionProcessButton) findViewById(R.id.btnSignIn);
        button.setText("修改");
        //set name tv
        EditText nameEditText = (EditText)findViewById(R.id.et_name);
        Account account = (Account) AppContext.get(AppContext.KEY_ACCOUNT);
        if (account != null) {
            nameEditText.setText(account.getName());
        }
    }

    @Override
    public void doOnClickWork(String userId, String name, String gender) {
        final User newUser = new User(userId,name,gender,null,null,null);

        //在LBS云更新POI
//        LBSCloud.getInstance().updateUserProfile(newUser,new AjaxCallBack<String>() {
//            @Override
//            public void onSuccess(String s) {
//                Log.d("Weiyu","LBSCloud updateUserProfile return: "+s);
//                if (JSON.parseObject(s).getString("status").equals("0")){
//                    //save new user
//                    AppContext.getInstance().setUser(newUser);
//                    ACache.getPermanence(ProfileActivity.this).put(AppContext.USER,newUser);
//                    //show
//                    showOnSuccessMsg("修改成功");
//                }
//                else {
//                    showOnErrorMsg("修改错误");
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t, int errorNo, String strMsg) {
//                Log.w("Weiyu",strMsg);
//                showOnErrorMsg("网络异常");
//            }
//        });

        //刷新RongCloud用户信息
        RongCloud.getInstance(this).refresh(userId,name,null,null);
    }
}
