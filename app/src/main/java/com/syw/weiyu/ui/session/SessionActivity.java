package com.syw.weiyu.ui.session;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.syw.weiyu.R;
import com.syw.weiyu.ui.user.UserHomeActivity;

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
        final String targetId = getIntent().getData().getQueryParameter("targetId");
        String title = getIntent().getData().getQueryParameter("title");
        ((TextView) findViewById(R.id.header_title)).setText(title);
        findViewById(R.id.header_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView userhome = (ImageView)findViewById(R.id.header_right);
        userhome.setImageResource(R.drawable.ic_userhome);
        userhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SessionActivity.this, UserHomeActivity.class);
                intent.putExtra("userId",targetId);
                SessionActivity.this.startActivity(intent);
            }
        });
    }

}
