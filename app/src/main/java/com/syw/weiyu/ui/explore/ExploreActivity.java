package com.syw.weiyu.ui.explore;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.syw.weiyu.R;

/**
 * author: youwei
 * date: 2015-05-11
 * desc: 发现（更多）
 */
public class ExploreActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_explore);

        findViewById(R.id.explore_item_nearby_people).setOnClickListener(this);

        ((TextView) findViewById(R.id.header_title)).setText("发现");
    }

    @Override
    public void onClick(View v) {
        Class activity = null;
        switch (v.getId()) {
            case R.id.explore_item_nearby_people:
                activity = NearByUserActivity.class;
                break;
        }
        Intent intent = new Intent(ExploreActivity.this, activity);
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
