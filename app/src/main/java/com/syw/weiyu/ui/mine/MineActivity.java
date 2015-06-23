package com.syw.weiyu.ui.mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import com.orhanobut.logger.Logger;
import com.syw.weiyu.R;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.bean.ShuoshuoList;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.core.App;
import com.syw.weiyu.core.WeiyuApi;
import com.syw.weiyu.ui.explore.AboutInfoActivity;
import com.syw.weiyu.ui.explore.NearByUserActivity;
import com.syw.weiyu.ui.user.ProfileActivity;
import com.syw.weiyu.ui.user.UserHomeActivity;

import java.util.List;

/**
 * author: youwei
 * date: 2015-05-11
 * desc: 我
 */
public class MineActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_mine);

        findViewById(R.id.explore_item_mine).setOnClickListener(this);
        findViewById(R.id.explore_item_profile).setOnClickListener(this);
        findViewById(R.id.explore_item_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MineActivity.this,AlertDialog.THEME_HOLO_LIGHT);
                builder.setMessage("退出后将无法收到消息，确定要退出嘛？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                WeiyuApi.get().logout();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create().show();
            }
        });
        findViewById(R.id.explore_item_aboutinfo).setOnClickListener(this);

        ((TextView) findViewById(R.id.header_title)).setText("我");
    }

    @Override
    public void onClick(View v) {
        Class activity = null;
        switch (v.getId()) {
            case R.id.explore_item_mine:
                activity = UserHomeActivity.class;
                break;
            case R.id.explore_item_profile:
                activity = ProfileActivity.class;
                break;
            case R.id.explore_item_aboutinfo:
                activity = AboutInfoActivity.class;
                break;
        }
        Intent intent = new Intent(MineActivity.this, activity);
        startActivity(intent);
    }


//    /**
//     * 两次返回键退出，间隔2秒
//     */
//    private long currentTime=0;
//    private long oldTime=0;
//    @Override
//    public void onBackPressed() {
//        currentTime = System.currentTimeMillis();
//        if ((currentTime - oldTime) > 2000 || oldTime == 0) {
//            Toast.makeText(this, "再按一次退出", 2000).show();
//            oldTime = currentTime;
//        } else {
//            finish();
//        }
//    }
}
