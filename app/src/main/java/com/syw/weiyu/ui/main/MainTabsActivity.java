package com.syw.weiyu.ui.main;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import com.syw.weiyu.R;

@SuppressWarnings("deprecation")
public class MainTabsActivity extends TabActivity {
	private TabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.wy_activity_maintabs);
		initViews();
		initTabs();
	}

    private void initViews() {
		mTabHost = getTabHost();
	}

	private void initTabs() {
		LayoutInflater inflater = LayoutInflater.from(MainTabsActivity.this);

        /**
         * 附近的说说
         */
        //底部指示器视图，上面图标下面文字
		View nearbyView = inflater.inflate(
				R.layout.common_bottombar_tab_nearby_shuoshuo, null);
		TabHost.TabSpec nearbyTabSpec = mTabHost.newTabSpec(
				ShuoshuoActivity.class.getName()).setIndicator(nearbyView);
        //设置上面的内容
		nearbyTabSpec.setContent(new Intent(MainTabsActivity.this,
                ShuoshuoActivity.class));
        //添加进去
		mTabHost.addTab(nearbyTabSpec);

        /**
         * 消息
         */
		View sessionListView = inflater.inflate(
				R.layout.common_bottombar_tab_chat, null);
		TabHost.TabSpec sessionListTabSpec = mTabHost.newTabSpec(
                SessionListActivity.class.getName()).setIndicator(
				sessionListView);
		sessionListTabSpec.setContent(new Intent(MainTabsActivity.this,
				SessionListActivity.class));
		mTabHost.addTab(sessionListTabSpec);

        /**
         * 发现
         */
        View exploreView = inflater.inflate(
                R.layout.common_bottombar_tab_explore, null);
        TabHost.TabSpec exploreTabSpec = mTabHost.newTabSpec(
                ExploreActivity.class.getName()).setIndicator(
                exploreView);
        exploreTabSpec.setContent(new Intent(MainTabsActivity.this,
                ExploreActivity.class));
        mTabHost.addTab(exploreTabSpec);

	}
}
