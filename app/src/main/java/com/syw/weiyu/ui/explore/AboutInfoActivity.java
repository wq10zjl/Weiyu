package com.syw.weiyu.ui.explore;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import android.widget.TextView;
import com.syw.weiyu.R;

public class AboutInfoActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_about_info);

        findViewById(R.id.header_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.header_title)).setText("关于");
    }

}
