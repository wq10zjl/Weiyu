package com.syw.weiyu.ui.session;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.syw.weiyu.R;

/**
 * author: youwei
 * date: 2015-05-11
 * desc: 会话列表（消息）
 */
public class SessionListActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_sessionlist);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.sessionlist_item_comment_message).setOnClickListener(this);

        ((TextView) findViewById(R.id.header_title)).setText("消息");
    }

    @Override
    public void onClick(View v) {
        Class activity = null;
        switch (v.getId()) {
            case R.id.sessionlist_item_comment_message:
                activity = CommentMessageActivity.class;
                break;
        }
        Intent intent = new Intent(SessionListActivity.this, activity);
        startActivity(intent);
    }
}
