package com.syw.weiyu.ui.session;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.syw.weiyu.R;

/**
 * author: youwei
 * date: 2015-05-11
 * desc: 会话列表（消息）
 */
public class SessionListActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_sessionlist);
        initViews();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.header_title)).setText("消息");
    }

}
