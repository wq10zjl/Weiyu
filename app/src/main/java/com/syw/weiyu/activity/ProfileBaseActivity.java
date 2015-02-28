package com.syw.weiyu.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.syw.weiyu.R;
import com.syw.weiyu.view.SwitcherButton;

public abstract class ProfileBaseActivity extends FragmentActivity {

    protected String userId;
    protected String name;
    protected String gender;
    protected String hobby;

    protected ActionProcessButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_login);

        initGenderSwitcher();
        initHobbySwitcher();
        initSignInButton();
    }

    /**
     * 按钮点击后做的操作
     */
    protected abstract void doOnClickWork();

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
                doOnClickWork();
            }
        });
    }

    private void initGenderSwitcher() {
        SwitcherButton genderSwitcherButton = (SwitcherButton) findViewById(R.id.switcher_gender).findViewById(R.id.header_sb_rightview_switcherbutton);
        final LinearLayout genderSwitcherTab = (LinearLayout) genderSwitcherButton.findViewById(R.id.switcher_layout_tab);

        //设置切换按钮文本
        TextView leftTextView = (TextView) genderSwitcherButton.findViewById(R.id.switcher_tv_left_text);
        TextView rightTextView = (TextView) genderSwitcherButton.findViewById(R.id.switcher_tv_right_text);
        leftTextView.setText("男");
        rightTextView.setText("女");

        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_fadein,
                R.anim.fragment_fadeout);

        //默认男
        genderSwitcherTab.setGravity(Gravity.LEFT);
        gender = "男";

        //男女监听切换按钮
        genderSwitcherButton.setOnSwitcherButtonClickListener(new SwitcherButton.onSwitcherButtonClickListener() {
            @Override
            public void onClick(SwitcherButton.SwitcherButtonState state) {
                switch (state) {
                    case LEFT:
                        //左边是“男”
                        genderSwitcherTab.setGravity(Gravity.LEFT);
                        gender = "男";
                        break;

                    case RIGHT:
                        //右边是“女”
                        genderSwitcherTab.setGravity(Gravity.RIGHT);
                        gender = "女";
                        break;
                }
            }
        });
    }

    private void initHobbySwitcher() {
        SwitcherButton hobbySwitcherButton = (SwitcherButton) findViewById(R.id.switcher_hobby).findViewById(R.id.header_sb_rightview_switcherbutton);
        final LinearLayout hobbySwitcherTab = (LinearLayout) hobbySwitcherButton.findViewById(R.id.switcher_layout_tab);

        //设置切换按钮文本
        TextView leftTextView = (TextView) hobbySwitcherButton.findViewById(R.id.switcher_tv_left_text);
        TextView rightTextView = (TextView) hobbySwitcherButton.findViewById(R.id.switcher_tv_right_text);
        leftTextView.setText("男");
        rightTextView.setText("女");

        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_fadein,
                R.anim.fragment_fadeout);

        //默认女
        hobbySwitcherTab.setGravity(Gravity.RIGHT);
        hobby = "女";
   
        //男女监听切换按钮
        hobbySwitcherButton.setOnSwitcherButtonClickListener(new SwitcherButton.onSwitcherButtonClickListener() {
            @Override
            public void onClick(SwitcherButton.SwitcherButtonState state) {
                switch (state) {
                    case LEFT:
                        //左边是“男”
                        hobbySwitcherTab.setGravity(Gravity.LEFT);
                        hobby = "男";
                        break;

                    case RIGHT:
                        //右边是“女”
                        hobbySwitcherTab.setGravity(Gravity.RIGHT);
                        hobby = "女";
                        break;
                }
            }
        });
    }
}
