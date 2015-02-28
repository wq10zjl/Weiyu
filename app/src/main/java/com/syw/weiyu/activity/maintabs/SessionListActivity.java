package com.syw.weiyu.activity.maintabs;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.syw.weiyu.R;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.fragment.ConversationSettingFragment;

/**
 * Created by songyouwei on 2015/2/9.
 */
public class SessionListActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_sessionlist);
        getSupportFragmentManager().beginTransaction().replace(R.id.sessionlist_layout_content,new ConversationListFragment()).commit();
    }
}
