package com.syw.weiyu.ui.main;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.syw.weiyu.R;

import io.rong.imkit.fragment.ConversationListFragment;

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
        getSupportFragmentManager().beginTransaction().replace(R.id.sessionlist_layout_content,new ConversationListFragment()).commit();
    }

    private void initViews() {
        TextView tvTitle = (TextView) findViewById(R.id.header_tv_title);
        tvTitle.setText("消息");
    }

}
