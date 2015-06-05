package com.syw.weiyu.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import com.syw.weiyu.R;
import com.syw.weiyu.ui.explore.AboutInfoActivity;
import com.syw.weiyu.ui.explore.NearByUserActivity;
import com.syw.weiyu.ui.user.ProfileActivity;

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

        findViewById(R.id.explore_item_profile).setOnClickListener(this);
        findViewById(R.id.explore_item_aboutinfo).setOnClickListener(this);

        ((TextView) findViewById(R.id.header_title)).setText("我");
    }

    @Override
    public void onClick(View v) {
        Class activity = null;
        switch (v.getId()) {
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
