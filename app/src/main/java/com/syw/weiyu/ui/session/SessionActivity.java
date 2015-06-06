package com.syw.weiyu.ui.session;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import com.syw.weiyu.R;

/**
 * author: youwei
 * date: 2015-06-01
 * desc:
 */
public class SessionActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_session);
        initViews();
    }

    private void initViews() {
        String title = getIntent().getData().getQueryParameter("title");
        ((TextView) findViewById(R.id.header_title)).setText(title);
        findViewById(R.id.header_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
