package com.syw.weiyu.ui.user;

import android.content.Context;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dd.processbutton.iml.ActionProcessButton;
import com.syw.weiyu.R;
import com.syw.weiyu.util.StringUtil;

import java.util.Random;

public abstract class ProfileBaseActivity extends FragmentActivity {

    protected String userId;
    protected String name;
    protected String gender;

    protected ActionProcessButton btnSignIn;

    protected RadioGroup genderRG;
    protected RadioButton manRD;
    protected RadioButton womanRD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_login);

        initGenderRadio();
        initSignInButton();
    }

    /**
     * 初始化性别选择Radio
     */
    private void initGenderRadio() {
        genderRG = (RadioGroup) findViewById(R.id.rg_gender);
        manRD = (RadioButton) findViewById(R.id.rb_man);
        womanRD = (RadioButton) findViewById(R.id.rb_woman);
    }

    /**
     * 提交按钮点击后做的操作
     */
    protected abstract void doOnClickWork(String userId, String name, String gender);

    /**
     * 发生错误后的操作
     * @param msg 错误信息，会显示在按钮上
     */
    protected void showOnErrorMsg(String msg) {
        btnSignIn.setProgress(-1);
        btnSignIn.setText(msg);
    }

    /**
     * 正确操作结束后的操作
     * @param msg 完成信息，会显示在按钮上
     */
    protected void showOnSuccessMsg(String msg) {
        btnSignIn.setProgress(100);
        btnSignIn.setText(msg);
    }

    private void initSignInButton() {
        btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        // you can display endless google like progress indicator
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set progress > 0 to start progress indicator animation
                btnSignIn.setProgress(1);
                btnSignIn.setEnabled(false);

                userId = getUniqueID();
                if (StringUtil.isEmpty(name)) name = "匿名";
                gender = genderRG.getCheckedRadioButtonId() == manRD.getId() ? "男" : "女";
                doOnClickWork(userId, name, gender);
            }
        });
    }

    /**
     * 获取设备的唯一识别码
     * @return
     */
    private String getUniqueID(){
        String myAndroidDeviceId = "";
        TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null){
            myAndroidDeviceId = mTelephony.getDeviceId();
        }else{
            myAndroidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return myAndroidDeviceId;
    }
}
